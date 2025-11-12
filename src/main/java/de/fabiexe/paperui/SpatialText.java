package de.fabiexe.paperui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpatialText {
    Vector position;
    SpatialUI.VerticalAlignment verticalAlignment;
    Component text;
    TextDisplay.TextAlignment textAlignment;
    Integer backgroundColor;

    SpatialText(@NotNull Vector position,
                @NotNull SpatialUI.VerticalAlignment verticalAlignment,
                @NotNull Component text,
                @NotNull TextDisplay.TextAlignment textAlignment,
                @Nullable Integer backgroundColor) {
        this.position = position;
        this.verticalAlignment = verticalAlignment;
        this.text = text;
        this.textAlignment = textAlignment;
        this.backgroundColor = backgroundColor;
    }

    public @NotNull Vector getPosition() {
        return position;
    }

    public void setPosition(@NotNull Vector position) {
        this.position = position;
    }

    public @NotNull SpatialUI.VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(@NotNull SpatialUI.VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public @NotNull Component getText() {
        return text;
    }

    public void setText(@NotNull Component text) {
        this.text = text;
    }

    public @NotNull TextDisplay.TextAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(@NotNull TextDisplay.TextAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public @Nullable Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@Nullable Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
