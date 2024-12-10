package dev.vansen.commandutils.completer;

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
@FunctionalInterface
public interface CompletionHandler {

    /**
     * Generates completion suggestions based on the provided command context and suggestions builder.
     * Note, this method will be called with a different SuggestionsBuilderWrapper each time, so storing the SuggestionsBuilderWrapper for future use is not recommended.
     *
     * @param context the {@link CommandWrapper} containing context information for the command.
     * @param wrapper the {@link SuggestionsBuilderWrapper} used to build and provide suggestions.
     * @return a {@link CompletableFuture} that will be completed with the {@link Suggestions} for tab completion.
     * The future will contain the suggestions generated based on the provided context and wrapper.
     */
    @NotNull
    CompletableFuture<Suggestions> complete(@NotNull CommandWrapper context, @NotNull SuggestionsBuilderWrapper wrapper);
}