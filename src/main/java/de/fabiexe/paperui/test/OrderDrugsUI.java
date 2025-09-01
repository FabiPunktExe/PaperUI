package de.fabiexe.paperui.test;

import de.fabiexe.paperui.DialogUI;
import de.fabiexe.paperui.property.Property;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class OrderDrugsUI extends DialogUI<Audience> {
    public OrderDrugsUI(@NotNull Audience audience) {
        super(audience);

        title("Order drugs");
        Property<DrugType> type = enumProperty("Type", DrugType.class);
        Property<Double> dosis = doubleProperty("Dosis (%)", 50, 120, 100);
        Property<Integer> amount = integerProperty("Amount", 1, 10);
        Property<String> address = stringProperty("Address");

        actionButton("Order", () -> {
            String drug = type.get() + " with dosis " + dosis.get() + "%";
            audience.sendMessage(Component.text("We will deliver " + amount.get() + " " + drug + " to " + address.get()));
        });
        actionButton("Cancel");
    }

    private enum DrugType {
        CANNABIS, COCAINE, HEROIN, FENTANYL
    }
}
