package dev.vansen.commandutils.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an argument in a command.
 *
 * @param name The name of the argument.
 * @param type The type of the argument.
 */
public record Argument(@NotNull String name, @NotNull ArgumentType<?> type) {
}
