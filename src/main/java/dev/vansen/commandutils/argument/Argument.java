package dev.vansen.commandutils.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an argument in a command.
 *
 * @param <T> the type of the argument
 */
public record Argument<T>(@NotNull String name, @NotNull ArgumentType<T> type) {
}
