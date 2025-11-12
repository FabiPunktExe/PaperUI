package de.fabiexe.paperui.property;

import de.fabiexe.paperui.SpatialUI;
import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpatialUIProperty<T> implements MutableProperty<T> {
    private final SpatialUI ui;
    private T value;

    public SpatialUIProperty(@NotNull SpatialUI ui, T value) {
        this.ui = ui;
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    /**
     * Set the value of this property
     *
     * @param value The new value
     */
    public void set(T value) {
        this.value = value;
        ui.render();
    }

    /**
     * Do not use this method directly, it is used by Kotlin's property delegation.
     * Use {@code by} keyword in Kotlin or {@link SpatialUIProperty#set(T)} in Java instead.
     *
     * @param nothing Nothing
     * @param property A property
     * @param value The new value
     */
    public void setValue(@Nullable Object nothing, @NotNull KProperty<?> property, T value) {
        set(value);
    }
}
