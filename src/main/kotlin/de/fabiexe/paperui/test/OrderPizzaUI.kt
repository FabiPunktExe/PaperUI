package de.fabiexe.paperui.test

import de.fabiexe.paperui.DialogUI
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component

fun create(audience: Audience) = DialogUI(audience).apply {
    title("Order a Pizza")
    val type by enumProperty("Type", PizzaType::class.java)
    val extraCheese by booleanProperty("Add Extra Cheese")
    val radius by doubleProperty("Radius (cm)", 15.0, 20.0)
    val amount by integerProperty("Amount", 1, 10, 1)
    val address by stringProperty("Address")

    actionButton("Order") {
        var pizza = "$type with radius ${radius}cm"
        if (extraCheese) {
            pizza += " with extra cheese"
        }
        audience.sendMessage(Component.text("We will deliver $amount $pizza to $address"))
    }
    actionButton("Cancel")
}

enum class PizzaType {
    MARGHERITA, FUNGHI, PROSCIUTTO
}
