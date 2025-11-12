package de.fabiexe.paperui.test

import de.fabiexe.paperui.PaperUIPlugin
import de.fabiexe.paperui.SpatialUI
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay

val phones = listOf(
    Phone(PhoneType.SUMSUM_GALAX_E, 1, 699.99),
    Phone(PhoneType.SUMSUM_GALAX_E, 2, 799.99),
    Phone(PhoneType.SUMSUM_GALAX_E, 3, 899.99),
    Phone(PhoneType.EYE_PHONE, 1, 999.99),
    Phone(PhoneType.EYE_PHONE, 2, 1299.99),
    Phone(PhoneType.EYE_PHONE, 3, 1499.99),
    Phone(PhoneType.GOGGLE_PIG_SELL, 1, 499.99)
)

fun create(audience: Player) = SpatialUI(
    audience,
    PaperUIPlugin.getInstance().packetEventsAPI,
    audience.location,
    audience.facing.oppositeFace
).apply {
    var selectedPhone by property<Phone?>(null)

    content {
        text(0.0, 2.0, "<underlined>Order a Phone")

        if (selectedPhone == null) {
            val names = phones.joinToString("\n") { "${it.type.name} v${it.version}" }
            val prices = phones.joinToString("\n") { "$${it.price}" }
            text(0.0, 0.0, names).textAlignment = TextDisplay.TextAlignment.LEFT
            text(2.0, 0.0, prices).textAlignment = TextDisplay.TextAlignment.RIGHT

            var yOffset = 1.5
            for (phone in phones) {
                button(0.0, yOffset, 2.5, 0.25) {
                    selectedPhone = phone
                }
                yOffset -= 0.25
            }
        } else {
            text(0.0, 1.25, "You have selected:\n${selectedPhone!!.type.name} v${selectedPhone!!.version} for $${selectedPhone!!.price}")
            text(-1.0, 0.75, "<red>Cancel Order").textAlignment = TextDisplay.TextAlignment.CENTER
            text(1.0, 0.75, "<green>Confirm Order").textAlignment = TextDisplay.TextAlignment.CENTER
            button(-1.0, 0.75, 1.5, 0.25) {
                selectedPhone = null
            }
            button(1.0, 0.75, 1.5, 0.25) {
                audience.sendMessage("You have ordered a ${selectedPhone!!.type.name} v${selectedPhone!!.version} for $${selectedPhone!!.price}!")
                selectedPhone = null
            }
        }
    }
}

enum class PhoneType {
    SUMSUM_GALAX_E, EYE_PHONE, GOGGLE_PIG_SELL
}

data class Phone(val type: PhoneType, val version: Int, val price: Double)