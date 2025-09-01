package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComponentProperty implements Property<Component> {
    private final String key;
    private final Component label;
    private final int maxLines;
    private Component value;

    public ComponentProperty(@NotNull String key, @NotNull Component label, int maxLines, @Nullable Component value) {
        this.key = key;
        this.label = label;
        this.maxLines = maxLines;
        this.value = value;
    }

    @Override
    public @NotNull Component get() {
        if (value == null) {
            throw new IllegalStateException("There is no value present");
        }
        return value;
    }

    @Override
    public void read(@NotNull DialogResponseView response) {
        value = MiniMessage.miniMessage().deserialize(response.getText(key));
    }

    @Override
    public void populate(@NotNull List<DialogInput> inputs) {
        TextDialogInput.Builder builder = DialogInput.text(key, label);
        if (maxLines != 1) {
            builder.multiline(TextDialogInput.MultilineOptions.create(maxLines, null));
        }
        if (value != null) {
            builder.initial(MiniMessage.miniMessage().serialize(value));
        }
        inputs.add(builder.build());
    }
}
