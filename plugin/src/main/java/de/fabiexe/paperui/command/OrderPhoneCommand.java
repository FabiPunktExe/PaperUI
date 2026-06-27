package de.fabiexe.paperui.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.fabiexe.paperui.test.OrderPhoneUIKt;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OrderPhoneCommand {
    public static @NotNull LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("order-phone")
                .requires(stack ->
                        stack.getExecutor() instanceof Player &&
                                stack.getSender().hasPermission("paperui.order-phone"))
                .executes(context -> {
                    OrderPhoneUIKt.create((Player) Objects.requireNonNull(context.getSource().getExecutor()));
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
