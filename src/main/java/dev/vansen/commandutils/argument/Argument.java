package dev.vansen.commandutils.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import org.jetbrains.annotations.NotNull;

public record Argument<T>(@NotNull String name, @NotNull ArgumentType<T> type) {
}
