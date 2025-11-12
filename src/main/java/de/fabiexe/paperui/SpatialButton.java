package de.fabiexe.paperui;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SpatialButton {
    Vector position;
    double width;
    double height;
    Runnable onClick;

    SpatialButton(@NotNull Vector position,
                  double width,
                  double height,
                  @NotNull Runnable onClick) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.onClick = onClick;
    }

    public @NotNull Vector getPosition() {
        return position;
    }

    public void setPosition(@NotNull Vector position) {
        this.position = position;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
