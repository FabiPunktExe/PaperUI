package de.fabiexe.paperui.test;

import de.fabiexe.paperui.DialogUI;
import de.fabiexe.paperui.property.Property;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class OrderCoffeeUI extends DialogUI<Audience> {
    private final Property<CoffeeType> type;
    private final Property<Double> size;
    private final Property<Boolean> milk;
    private final Property<Integer> amount;
    private final Property<String> address;

    public OrderCoffeeUI(@NotNull Audience audience) {
        super(audience);

        title("Order a Coffee");
        type = enumProperty("Type", CoffeeType.class);
        size = doubleProperty("Size (liters)", 0.25, 1, 0.33);
        milk = booleanProperty("Add milk");
        amount = integerProperty("Amount", 1, 10);
        address = stringProperty("Address");

        actionButton("Order", this::order);
        actionButton("Cancel");
    }

    private void order() {
        String coffee = type.get() + " with size " + size.get() + "l";
        if (milk.get()) {
            coffee += " with milk";
        }
        audience.sendMessage(Component.text("We will deliver " + amount.get() + " " + coffee + " to " + address.get()));
    }

    private enum CoffeeType {
        COFFEE, ESPRESSO, CHOCOLATE_COFFEE, DOUBLE_CHOCOLATE_COFFEE
    }
}
