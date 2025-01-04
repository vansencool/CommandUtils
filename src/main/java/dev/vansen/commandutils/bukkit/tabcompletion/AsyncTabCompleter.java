package dev.vansen.commandutils.bukkit.tabcompletion;

import dev.vansen.commandutils.bukkit.argument.BukkitArgument;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * An asynchronous tab completer that provides suggestions for a command.
 * <p>
 * This interface is designed to handle asynchronous tab completion, allowing
 * for more complex and time-consuming operations to be performed without
 * blocking the main thread.
 * <p>
 * Note that this is not simply a matter of calling {@link CompletableFuture#join()}
 * or {@link CompletableFuture#get()} on a future. Instead, this interface is
 * designed to allow for true asynchronous tab completion, where the completer
 * can perform its work in the background without blocking the main thread.
 */
@SuppressWarnings("unused")
public interface AsyncTabCompleter {

    @Nullable
    CompletableFuture<List<String>> tabComplete(@NotNull BukkitArgument argument, @NotNull SuggestionsBuilderWrapper wrapper);
}
