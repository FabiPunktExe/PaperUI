package de.fabiexe.paperui.test

import de.fabiexe.paperui.DialogUI
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

fun create(audience: Audience) = DialogUI(audience).apply {
    title("Order a Pizza")
    val type = enumProperty("Type", PizzaType::class.java)
    val extraCheese = booleanProperty("Add Extra Cheese")
    val radius = doubleProperty("Radius (cm)", 15.0, 20.0)
    val amount = integerProperty("Amount", 1, 10, 1)
    val address = stringProperty("Address")

    actionButton("Order") {
        var pizza = "${type.get()} with radius ${radius.get()}cm"
        if (extraCheese.get()) {
            pizza += " with extra cheese"
        }
        audience.sendMessage(Component.text("We will deliver ${amount.get()} $pizza to ${address.get()}"))
    }
    actionButton("Cancel")
}

enum class PizzaType {
    MARGHERITA, FUNGHI, PROSCIUTTO
}
