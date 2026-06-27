package de.fabiexe.paperui.spatial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata
import io.github.retrooper.packetevents.util.SpigotReflectionUtil
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.entity.Interaction
import org.bukkit.entity.Player
import org.bukkit.util.Vector

@Composable
fun Button(
    position: Vector,
    width: Float,
    height: Float,
    onClick: () -> Unit
) {
    val (player, packetEventsApi, level, uiPosition, direction) = LocalSpatialUIContext.current

    val interaction = rememberEntity(
        player,
        packetEventsApi,
        level,
        EntityTypes.INTERACTION,
        ::Interaction
    )

    LaunchedEffect(interaction, width, height) {
        if (interaction.width != width || interaction.height != height) {
            interaction.width = width
            interaction.height = height

            val packet = WrapperPlayServerEntityMetadata(
                interaction.id,
                SpigotReflectionUtil.getEntityMetadata(interaction.bukkitEntity)
            )
            packetEventsApi.playerManager.sendPacket(player, packet)
        }
    }

    DisposableEffect(interaction, onClick) {
        val listener = InteractionListener(player, interaction, onClick)
        packetEventsApi.eventManager.registerListener(listener)
        onDispose {
            packetEventsApi.eventManager.unregisterListener(listener)
        }
    }

    val position = position.clone().add(Vector(0.0, 0.0, width / -2.0))
    EntityPositionEffect(player, packetEventsApi, interaction, position, uiPosition, direction)
    EntityDestroyEffect(player, packetEventsApi, interaction)
}

private class InteractionListener(val player: Player, val interaction: Interaction, val onClick: () -> Unit) : PacketListenerAbstract() {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        if (event.getPlayer<Player>() != player || event.packetType != PacketType.Play.Client.INTERACT_ENTITY) {
            return
        }

        val packet = WrapperPlayClientInteractEntity(event)
        if (packet.action == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return
        }

        if (packet.entityId == interaction.id) {
            event.isCancelled = true
            onClick()
        }
    }
}

@Composable
fun Button(
    x: Double,
    y: Double,
    width: Float,
    height: Float,
    onClick: () -> Unit
) {
    Button(Vector(x, y, 0.0), width, height, onClick)
}