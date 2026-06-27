package de.fabiexe.paperui.test

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.retrooper.packetevents.PacketEvents
import de.fabiexe.paperui.spatial.Button
import de.fabiexe.paperui.spatial.SpatialUI
import de.fabiexe.paperui.spatial.Text
import de.fabiexe.paperui.spatial.launchSpatialUI
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

fun create(player: Player) = launchSpatialUI {
    SpatialUI(player, PacketEvents.getAPI(), player.location.toVector(), player.facing.oppositeFace) {
        var selectedPhone by remember { mutableStateOf<Phone?>(null) }

        Text(0.0, 2.0, "<underlined>Order a Phone")

        if (selectedPhone == null) {
            val names = phones.joinToString("\n") { "${it.type.prettyName()} v${it.version}" }
            val prices = phones.joinToString("\n") { "$${it.price}" }
            Text(0.0, 0.0, names, TextDisplay.TextAlignment.LEFT)
            Text(2.0, 0.0, prices, TextDisplay.TextAlignment.RIGHT)

            var yOffset = 1.5
            for (phone in phones) {
                Button(0.0, yOffset, 2.5f, 0.25f) {
                    selectedPhone = phone
                }
                yOffset -= 0.25
            }
        } else {
            Text(0.0, 1.25, "You have selected:\n${selectedPhone!!.type.prettyName()} v${selectedPhone!!.version} for $${selectedPhone!!.price}")
            Text(-1.0, 0.75, "<red>Cancel Order", TextDisplay.TextAlignment.CENTER)
            Text(1.0, 0.75, "<green>Confirm Order", TextDisplay.TextAlignment.CENTER)
            Button(-1.0, 0.75, 1.5f, 0.25f) {
                selectedPhone = null
            }
            Button(1.0, 0.75, 1.5f, 0.25f) {
                player.sendMessage("You have ordered a ${selectedPhone!!.type.prettyName()} v${selectedPhone!!.version} for $${selectedPhone!!.price}!")
                selectedPhone = null
            }
        }
    }
}

enum class PhoneType {
    SUMSUM_GALAX_E, EYE_PHONE, GOGGLE_PIG_SELL;

    fun prettyName(): String {
        return name.split("_").joinToString(" ") {
            it.lowercase().replaceFirstChar(Char::uppercase)
        }
    }
}

data class Phone(val type: PhoneType, val version: Int, val price: Double)