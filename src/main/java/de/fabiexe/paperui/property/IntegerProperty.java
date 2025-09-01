package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IntegerProperty implements Property<Integer> {
    private final String key;
    private final Component label;
    private final int min;
    private final int max;
    private Integer value;

    public IntegerProperty(@NotNull String key, @NotNull Component label, int min, int max, @Nullable Integer value) {
        this.key = key;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = value;
    }

    @Override
    public @NotNull Integer get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = Math.round(Math.max(min, Math.min(response.getFloat(key), max)));
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        NumberRangeDialogInput.Builder builder = DialogInput.numberRange(key, label, min, max).step(1f);
        if (value == null) {
            builder.initial((float) min);
        } else {
            builder.initial(value.floatValue());
        }
        inputs.add(builder.build());
    }
}
