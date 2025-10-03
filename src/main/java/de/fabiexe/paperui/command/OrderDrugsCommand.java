package de.fabiexe.paperui.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.fabiexe.paperui.test.OrderDrugsUI;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.NotNull;

public class OrderDrugsCommand {
    public static @NotNull LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("order-drugs")
                .requires(stack -> stack.getSender().hasPermission("paperui.order-drugs"))
                .executes(context -> {
                    OrderDrugsUI.create(context.getSource().getSender()).open();
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
