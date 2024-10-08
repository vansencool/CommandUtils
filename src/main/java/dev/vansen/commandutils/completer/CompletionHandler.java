package dev.vansen.commandutils.completer;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.vansen.commandutils.command.CommandWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * An interface for handling tab completion in commands.
 * Implementations of this interface should define how suggestions are generated based on the command context
 * and the provided {@link SuggestionsBuilderWrapper}.
 */
@SuppressWarnings("unused")
public interface CompletionHandler {

    /**
     * Generates completion suggestions based on the provided command context and suggestions builder.
     *
     * @param context the {@link CommandWrapper} containing context information for the command. This should not be null.
     * @param wrapper the {@link SuggestionsBuilderWrapper} used to build and provide suggestions. This should not be null.
     * @return a {@link CompletableFuture} that will be completed with the {@link Suggestions} for tab completion.
     * The future will contain the suggestions generated based on the provided context and wrapper.
     */
    @NotNull
    @CanIgnoreReturnValue
    CompletableFuture<Suggestions> complete(@NotNull CommandWrapper context, @NotNull SuggestionsBuilderWrapper wrapper);
}