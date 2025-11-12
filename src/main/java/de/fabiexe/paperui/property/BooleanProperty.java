package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.BooleanDialogInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BooleanProperty implements DialogProperty<Boolean> {
    private final String key;
    private final Component label;
    private Boolean value;

    public BooleanProperty(@NotNull String key, @NotNull Component label, @Nullable Boolean value) {
        this.key = key;
        this.label = label;
        this.value = value;
    }

    @Override
    public @NotNull Boolean get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = response.getBoolean(key);
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        BooleanDialogInput.Builder builder = DialogInput.bool(key, label);
        if (value != null) {
            builder.initial(value);
        }
        inputs.add(builder.build());
    }
}
