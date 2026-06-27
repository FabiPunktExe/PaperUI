package de.fabiexe.paperui.spatial

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot
import com.github.retrooper.packetevents.PacketEventsAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.server.level.ServerLevel
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.time.Duration.Companion.seconds

fun launchSpatialUI(content: @Composable () -> Unit) {
    val clock = BroadcastFrameClock()
    val coroutineScope = CoroutineScope(Dispatchers.Default + clock)
    val recomposer = Recomposer(coroutineScope.coroutineContext)
    val composition = Composition(NoopApplier, recomposer)

    coroutineScope.launch { recomposer.runRecomposeAndApplyChanges() }
    coroutineScope.launch {
        while (true) {
            clock.sendFrame(System.nanoTime())
            delay((1.0 / 20).seconds)
        }
    }

    composition.setContent(content)

    Snapshot.registerGlobalWriteObserver {
        Snapshot.sendApplyNotifications()
    }
}

data class SpatialUIContext(
    val player: Player,
    val packetEventsApi: PacketEventsAPI<*>,
    val level: ServerLevel,
    val position: Vector,
    val direction: BlockFace
)

val LocalSpatialUIContext = compositionLocalOf<SpatialUIContext> { error("CompositionLocal context not initialized") }

@Composable
fun SpatialUI(
    player: Player,
    packetEventsApi: PacketEventsAPI<*>,
    position: Vector,
    direction: BlockFace,
    content: @Composable () -> Unit
) {
    val level = remember { (player as CraftPlayer).handle.level() }
    val context = SpatialUIContext(player, packetEventsApi, level, position, direction)
    CompositionLocalProvider(LocalSpatialUIContext provides context, content)
}