package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringProperty implements Property<String> {
    private final String key;
    private final Component label;
    private final int maxLines;
    private String value;

    public StringProperty(@NotNull String key, @NotNull Component label, int maxLines, @Nullable String value) {
        this.key = key;
        this.label = label;
        this.maxLines = maxLines;
        this.value = value;
    }

    @Override
    public @NotNull String get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = response.getText(key);
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        TextDialogInput.Builder builder = DialogInput.text(key, label);
        if (maxLines == 1) {
            builder.multiline(TextDialogInput.MultilineOptions.create(maxLines, null));
        }
        if (value != null) {
            builder.initial(value);
        }
        inputs.add(builder.build());
    }
}
