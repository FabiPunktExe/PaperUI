package de.fabiexe.paperui.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.fabiexe.paperui.test.OrderCoffeeUI;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;

public class OrderCoffeCommand {
    public static @NotNull LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("order-coffee")
                .requires(stack -> stack.getSender().hasPermission("paperui.order-coffee"))
                .executes(context -> {
                    new OrderCoffeeUI(context.getSource().getSender()).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
