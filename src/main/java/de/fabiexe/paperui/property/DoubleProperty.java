package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.NumberRangeDialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DoubleProperty implements DialogProperty<Double> {
    private final String key;
    private final Component label;
    private final double min;
    private final double max;
    private Double value;

    public DoubleProperty(@NotNull String key, @NotNull Component label, double min, double max, @Nullable Double value) {
        this.key = key;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = value;
    }

    @Override
    public @NotNull Double get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = (double) Math.max(min, Math.min(response.getFloat(key), max));
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        NumberRangeDialogInput.Builder builder = DialogInput.numberRange(key, label, (float) min, (float) max);
        builder.step(0.01f);
        if (value == null) {
            builder.initial((float) min);
        } else {
            builder.initial(value.floatValue());
        }
        inputs.add(builder.build());
    }
}
