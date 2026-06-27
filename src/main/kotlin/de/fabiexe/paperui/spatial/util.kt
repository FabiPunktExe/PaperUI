package de.fabiexe.paperui.spatial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.github.retrooper.packetevents.PacketEventsAPI
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.entity.CraftEntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector

@Composable
internal inline fun <T : Entity> rememberEntity(
    player: Player,
    packetEventsApi: PacketEventsAPI<*>,
    level: Level,
    entityType: EntityType<T>,
    crossinline constructor: (EntityType<T>, Level) -> T
): T {
    return remember(player, level) {
        val entity = constructor(entityType, level)
        val packet = WrapperPlayServerSpawnEntity(
            entity.id,
            entity.uuid,
            SpigotConversionUtil.fromBukkitEntityType(CraftEntityType.minecraftToBukkit(entityType)),
            SpigotConversionUtil.fromBukkitLocation(player.location),
            0f,
            0,
            Vector3d()
        )
        packetEventsApi.playerManager.sendPacket(player, packet)
        entity
    }
}

@Composable
fun EntityPositionEffect(
    player: Player,
    packetEventsApi: PacketEventsAPI<*>,
    entity: Entity,
    position: Vector,
    uiPosition: Vector,
    direction: BlockFace
) {
    LaunchedEffect(entity, position, uiPosition, direction) {
        var offset = position.clone().add(Vector(0.0, 0.0, 0.001))
        offset = Vector(
            offset.x * direction.modZ + offset.z * direction.modX,
            offset.y,
            -offset.x * direction.modX + offset.z * direction.modZ
        )

        val location = uiPosition.toLocation(player.world)
        location.direction = direction.direction
        location.add(offset)

        val oldPosition = entity.position()
        val newPosition = Vec3(location.x, location.y, location.z)
        if (oldPosition != newPosition || entity.yRot != location.yaw || entity.xRot != location.pitch) {
            entity.setPos(newPosition)
            entity.setRot(location.yaw, location.pitch)

            val packet = WrapperPlayServerEntityTeleport(
                entity.id,
                SpigotConversionUtil.fromBukkitLocation(location),
                false
            )
            packetEventsApi.playerManager.sendPacket(player, packet)
        }
    }
}

@Composable
fun EntityDestroyEffect(player: Player, packetEventsApi: PacketEventsAPI<*>, entity: Entity) {
    DisposableEffect(player, entity) {
        onDispose {
            packetEventsApi.playerManager.sendPacket(player, WrapperPlayServerDestroyEntities(entity.id))
        }
    }
}