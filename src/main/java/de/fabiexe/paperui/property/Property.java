package de.fabiexe.paperui.property;

import kotlin.reflect.KProperty;
import org.jetbrains.annotations.Nullable;

public interface Property<T> {
    /**
     * Get the value of this property
     *
     * @return The value
     * @throws IllegalStateException If the value is null and the property does not allow null values
     */
    T get();

    /**
     * Do not use this method directly, it is used by Kotlin's property delegation.
     * Use {@code by} keyword in Kotlin or {@link Property#get()} in Java instead.
     *
     * @param nothing Nothing
     * @param property A property
     * @return The value
     * @throws IllegalStateException If the value is null and the property does not allow null values
     */
    default T getValue(@Nullable Object nothing, @Nullable KProperty<?> property) {
        return get();
    }
}
