package de.fabiexe.paperui;

import de.fabiexe.paperui.command.OrderCoffeCommand;
import de.fabiexe.paperui.command.OrderDrugsCommand;
import de.fabiexe.paperui.command.OrderPizzaCommand;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PaperUIPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this::registerCommands);
    }

    private void registerCommands(@NotNull ReloadableRegistrarEvent<@NotNull Commands> event) {
        String enableDevCommands = System.getProperty("PAPERUI_ENABLE_DEV_COMMANDS");
        if (enableDevCommands != null && enableDevCommands.equals("true")) {
            event.registrar().register(OrderCoffeCommand.create());
            event.registrar().register(OrderDrugsCommand.create());
            event.registrar().register(OrderPizzaCommand.create());
        }
    }
}
