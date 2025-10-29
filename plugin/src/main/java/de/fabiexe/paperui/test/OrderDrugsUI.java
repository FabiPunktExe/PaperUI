package de.fabiexe.paperui.test;

import de.fabiexe.paperui.DialogUI;
import de.fabiexe.paperui.property.Property;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class OrderDrugsUI {
    public static DialogUI<Audience> create(@NotNull Audience audience) {
        DialogUI<Audience> ui = new DialogUI<>(audience);

        ui.title("Order drugs");

        Property<DrugType> type = ui.enumProperty("Type", DrugType.class);
        Property<Double> dosis = ui.doubleProperty("Dosis (%)", 50, 120, 100);
        Property<Integer> amount = ui.integerProperty("Amount", 1, 10);
        Property<String> address = ui.stringProperty("Address");

        ui.actionButton("Order", () -> {
            String drug = type.get() + " with dosis " + dosis.get() + "%";
            audience.sendMessage(Component.text("We will deliver " + amount.get() + " " + drug + " to " + address.get()));
        });
        ui.actionButton("Cancel");

        return ui;
    }

    private enum DrugType {
        CANNABIS, COCAINE, HEROIN, FENTANYL
    }
}
