package de.fabiexe.paperui;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Button(@NotNull String key,
                     @NotNull Component label,
                     @Nullable Component tooltip,
                     @Nullable Runnable action) {}
