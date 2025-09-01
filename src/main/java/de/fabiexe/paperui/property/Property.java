package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Property<T> {
    /**
     * Get the value of this property
     *
     * @return The value
     * @throws IllegalStateException If the value was not read yet
     */
    @NotNull T get();

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
