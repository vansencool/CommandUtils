package dev.vansen.commandutils.completer;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A wrapper class for {@link SuggestionsBuilder} to simplify and enhance the process of
 * building tab completion suggestions.
 * This class provides methods to add suggestions and build the completion results.
 */
@SuppressWarnings("unused")
public class SuggestionsBuilderWrapper {

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
     * Adds multiple suggestions to the list of completions.
     *
     * @param suggestions the suggestions to be added. This should not be null.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull String... suggestions) {
        Arrays.stream(suggestions)
                .forEach(builder::suggest);
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
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param suggestion the text to be suggested. This should not be null.
     * @param tooltip    the tooltip to be shown with the suggestion. This should not be null.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull String suggestion, @NotNull String tooltip) {
        builder.suggest(suggestion, Tooltiper.of(tooltip));
        return this;
    }

    /**
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param suggestion the suggestion to be added. This should not be null.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull Suggestion suggestion) {
        Optional.ofNullable(suggestion.tooltip())
                .ifPresentOrElse(
                        tooltip -> builder.suggest(suggestion.text(), tooltip),
                        () -> builder.suggest(suggestion.text()));
        return this;
    }

    /**
     * Returns an {@link CompletableFuture} containing an empty {@link Suggestions}.
     *
     * @return an {@link CompletableFuture} containing an empty {@link Suggestions}
     */
    @NotNull
    @CanIgnoreReturnValue
    public CompletableFuture<Suggestions> empty() {
        return Suggestions.empty();
    }

    /**
     * Builds and returns the suggestions as a {@link Suggestions}.
     * This will only work if this is called in a separate thread.
     *
     * @return a {@link Suggestions}.
     */
    @NotNull
    public Suggestions buildFuture() {
        return builder.build();
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