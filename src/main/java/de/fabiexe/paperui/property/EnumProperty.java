package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnumProperty<T> implements DialogProperty<T> {
    private final String key;
    private final Component label;
    private final List<T> values;
    private final Function<T, Component> nameFunction;
    private T value;

    public EnumProperty(@NotNull String key,
                        @NotNull Component label,
                        @NotNull T[] values,
                        @NotNull Function<T, Component> nameFunction,
                        @Nullable T value) {
        this.key = key;
        this.label = label;
        this.values = List.of(values);
        this.values.forEach(Objects::requireNonNull);
        this.nameFunction = nameFunction;
        this.value = value;
    }

    @Override
    public @NotNull T get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = values.get(Integer.parseInt(response.getText(key).replace("enum_", "")));
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        List<SingleOptionDialogInput.OptionEntry> options = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            options.add(SingleOptionDialogInput.OptionEntry.create(
                    "enum_" + i,
                    nameFunction.apply(values.get(i)),
                    values.get(i).equals(value)));
        }
        inputs.add(DialogInput.singleOption(key, label, options).build());
    }
}
