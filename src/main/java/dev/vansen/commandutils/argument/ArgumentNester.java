package dev.vansen.commandutils.argument;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Utility class for nesting arguments into a command.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class ArgumentNester {

    /**
     * Backend method for nesting arguments into a command, you generally don't need to use this.
     */
    public static void nest(@NotNull List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack, @NotNull LiteralArgumentBuilder<CommandSourceStack> builder) {
        if (argumentStack.isEmpty()) return;

        RequiredArgumentBuilder<CommandSourceStack, ?> lastArg = argumentStack.getLast();
        for (int i = argumentStack.size() - 2; i >= 0; i--) {
            RequiredArgumentBuilder<CommandSourceStack, ?> previousArg = argumentStack.get(i);
            previousArg.then(lastArg);
            lastArg = previousArg;
        }

        builder.then(lastArg);
    }

    /**
     * Backend method for nesting arguments into a command, you generally don't need to use this.
     */
    public static void nest(@NotNull RequiredArgumentBuilder<CommandSourceStack, ?> argument, @NotNull List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack) {
        if (argumentStack.isEmpty()) return;

        RequiredArgumentBuilder<CommandSourceStack, ?> lastArg = argumentStack.getLast();
        for (int i = argumentStack.size() - 2; i >= 0; i--) {
            RequiredArgumentBuilder<CommandSourceStack, ?> previousArg = argumentStack.get(i);
            previousArg.then(lastArg);
            lastArg = previousArg;
        }

        argument.then(lastArg);
    }
}