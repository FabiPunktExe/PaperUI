package de.fabiexe.paperui.property;

import kotlin.reflect.KProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableProperty<T> extends Property<T> {
    /**
     * Set the value of this property
     *
     * @param value The new value
     */
    void set(T value);

    /**
     * Do not use this method directly, it is used by Kotlin's property delegation.
     * Use {@code by} keyword in Kotlin or {@link MutableProperty#set(T)} in Java instead.
     *
     * @param nothing Nothing
     * @param property A property
     * @param value The new value
     */
    default void setValue(@Nullable Object nothing, @NotNull KProperty<?> property, T value) {
        set(value);
    }
}
