package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FloatProperty implements DialogProperty<Float> {
    private final String key;
    private final Component label;
    private final float min;
    private final float max;
    private Float value;

    public FloatProperty(@NotNull String key, @NotNull Component label, float min, float max, @Nullable Float value) {
        this.key = key;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = value;
    }

    @Override
    public @NotNull Float get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = Math.max(min, Math.min(response.getFloat(key), max));
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        NumberRangeDialogInput.Builder builder = DialogInput.numberRange(key, label, min, max);
        builder.step(0.01f);
        if (value == null) {
            builder.initial(min);
        } else {
            builder.initial(value);
        }
        inputs.add(builder.build());
    }
}
