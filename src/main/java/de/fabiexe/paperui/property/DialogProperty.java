package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface DialogProperty<T> extends Property<T> {
    /**
     * Read the value from a {@link DialogResponseView}
     *
     * @param response The response view
     */
    void read(@NotNull DialogResponseView response);

    /**
     * Populate a dialog
     *
     * @param inputs The dialog inputs
     */
    void populate(@NotNull List<DialogInput> inputs);
}
