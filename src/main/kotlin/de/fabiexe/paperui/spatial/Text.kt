package de.fabiexe.paperui.spatial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata
import io.github.retrooper.packetevents.util.SpigotReflectionUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityTypes
import org.bukkit.entity.TextDisplay
import org.bukkit.util.Vector

@Composable
fun Text(
    position: Vector,
    text: Component,
    alignment: TextDisplay.TextAlignment = TextDisplay.TextAlignment.CENTER
) {
    val (player, packetEventsApi, level, uiPosition, direction) = LocalSpatialUIContext.current

    val textDisplay = rememberEntity(
        player,
        packetEventsApi,
        level,
        EntityTypes.TEXT_DISPLAY,
        Display::TextDisplay
    )

    val bukkitTextDisplay = textDisplay.bukkitEntity as TextDisplay
    LaunchedEffect(textDisplay, text, alignment) {
        var dirty = false

        if (bukkitTextDisplay.text() != text) {
            bukkitTextDisplay.text(text)
            dirty = true
        }

        if (bukkitTextDisplay.alignment != alignment) {
            bukkitTextDisplay.alignment = alignment
            dirty = true
        }

        if (dirty) {
            val packet = WrapperPlayServerEntityMetadata(
                textDisplay.id,
                SpigotReflectionUtil.getEntityMetadata(bukkitTextDisplay)
            )
            packetEventsApi.playerManager.sendPacket(player, packet)
        }
    }

    EntityPositionEffect(player, packetEventsApi, textDisplay, position, uiPosition, direction)
    EntityDestroyEffect(player, packetEventsApi, textDisplay)
}

@Composable
fun Text(
    x: Double,
    y: Double,
    text: Component,
    alignment: TextDisplay.TextAlignment = TextDisplay.TextAlignment.CENTER
) {
    Text(Vector(x, y, 0.0), text, alignment)
}

@Composable
fun Text(
    position: Vector,
    text: String,
    alignment: TextDisplay.TextAlignment = TextDisplay.TextAlignment.CENTER
) {
    Text(position, MiniMessage.miniMessage().deserialize(text), alignment)
}

@Composable
fun Text(
    x: Double,
    y: Double,
    text: String,
    alignment: TextDisplay.TextAlignment = TextDisplay.TextAlignment.CENTER
) {
    Text(Vector(x, y, 0.0), MiniMessage.miniMessage().deserialize(text), alignment)
}