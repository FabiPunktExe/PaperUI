package de.fabiexe.paperui;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import de.fabiexe.paperui.command.OrderCoffeCommand;
import de.fabiexe.paperui.command.OrderDrugsCommand;
import de.fabiexe.paperui.command.OrderPhoneCommand;
import de.fabiexe.paperui.command.OrderPizzaCommand;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperUIPlugin extends JavaPlugin {
    private static PaperUIPlugin instance;
    private PacketEventsAPI<?> packetEventsAPI = null;

    @Override
    public void onLoad() {
        instance = this;
        if (PacketEvents.getAPI() == null) {
            packetEventsAPI = SpigotPacketEventsBuilder.build(this);
            PacketEvents.setAPI(packetEventsAPI);
            packetEventsAPI.load();
        }
    }

    @Override
    public void onEnable() {
        if (packetEventsAPI != null) {
            packetEventsAPI.init();
        }
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this::registerCommands);
    }

    @Override
    public void onDisable() {
        if (packetEventsAPI != null) {
            packetEventsAPI.terminate();
        }
    }

    private void registerCommands(@NotNull ReloadableRegistrarEvent<@NotNull Commands> event) {
        String enableDevCommands = System.getProperty("PAPERUI_ENABLE_DEV_COMMANDS");
        if (enableDevCommands != null && enableDevCommands.equals("true")) {
            event.registrar().register(OrderCoffeCommand.create());
            event.registrar().register(OrderDrugsCommand.create());
            event.registrar().register(OrderPhoneCommand.create());
            event.registrar().register(OrderPizzaCommand.create());
        }
    }

    public @NotNull PacketEventsAPI<?> getPacketEventsAPI() {
        return packetEventsAPI;
    }

    public static @NotNull PaperUIPlugin getInstance() {
        return instance;
    }
}
