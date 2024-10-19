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
     * Recursively nest arguments into the command.
     */
    public static void nest(@NotNull List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack, @NotNull LiteralArgumentBuilder<CommandSourceStack> builder) {
        if (argumentStack.isEmpty()) return;

        // start from the last argument and work backwards
        RequiredArgumentBuilder<CommandSourceStack, ?> lastArg = argumentStack.getLast();
        for (int i = argumentStack.size() - 2; i >= 0; i--) {
            RequiredArgumentBuilder<CommandSourceStack, ?> previousArg = argumentStack.get(i);
            previousArg.then(lastArg);  // nest the last argument into the previous one
            lastArg = previousArg;
        }

        builder.then(lastArg);
    }

    /**
     * Recursively nest arguments into the argument.
     */
    public static void nest(@NotNull RequiredArgumentBuilder<CommandSourceStack, ?> argument, @NotNull List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack) {
        if (argumentStack.isEmpty()) return;

        // start from the last argument and work backwards
        RequiredArgumentBuilder<CommandSourceStack, ?> lastArg = argumentStack.getLast();
        for (int i = argumentStack.size() - 2; i >= 0; i--) {
            RequiredArgumentBuilder<CommandSourceStack, ?> previousArg = argumentStack.get(i);
            previousArg.then(lastArg);  // nest the last argument into the previous one
            lastArg = previousArg;
        }

        argument.then(lastArg);
    }
}