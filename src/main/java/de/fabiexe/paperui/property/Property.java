package de.fabiexe.paperui.property;

import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import java.util.List;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Property<T> {
    /**
     * Get the value of this property
     *
     * @return The value
     * @throws IllegalStateException If the value was not read yet
     */
    @NotNull T get();

    /**
     * Do not use this method directly, it is used by Kotlin's property delegation.
     * Use {@code by} keyword in Kotlin or {@link Property#get()} in Java instead.
     *
     * @param nothing Nothing
     * @param property A property
     * @return The value
     * @throws IllegalStateException If the value was not read yet
     */
    default @NotNull T getValue(@Nullable Object nothing, @Nullable KProperty<?> property) {
        return get();
    }

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
