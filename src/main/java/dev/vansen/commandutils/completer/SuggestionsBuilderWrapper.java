package dev.vansen.commandutils.completer;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * A wrapper class for {@link SuggestionsBuilder} to simplify and enhance the process of
 * building tab completion suggestions.
 * This class provides methods to add suggestions and build the completion results.
 */
@SuppressWarnings("unused")
public class SuggestionsBuilderWrapper {

    /**
     * The underlying {@link SuggestionsBuilder} instance used to build suggestions.
     */
    private final @NotNull SuggestionsBuilder builder;

    /**
     * Constructs a new {@link SuggestionsBuilderWrapper} with the specified {@link SuggestionsBuilder}.
     *
     * @param builder the {@link SuggestionsBuilder} instance to wrap. This should not be null.
     */
    public SuggestionsBuilderWrapper(@NotNull SuggestionsBuilder builder) {
        this.builder = builder;
    }

    /**
     * Adds a suggestion to the list of completions.
     *
     * @param suggestion the text to be suggested. This should not be null.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull String suggestion) {
        builder.suggest(suggestion);
        return this;
    }

    /**
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param suggestion the text to be suggested. This should not be null.
     * @param tooltip    the tooltip to be shown with the suggestion. This should not be null.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull String suggestion, @NotNull Tooltiper tooltip) {
        builder.suggest(suggestion, tooltip);
        return this;
    }

    /**
     * Builds and returns the suggestions as a {@link CompletableFuture}.
     * This future will be completed with the suggestions once they are available.
     *
     * @return a {@link CompletableFuture} containing the {@link Suggestions}.
     */
    @NotNull
    public CompletableFuture<Suggestions> build() {
        return builder.buildFuture();
    }
}