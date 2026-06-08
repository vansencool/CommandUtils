package dev.vansen.commandutils.completer;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vansen.commandutils.completer.info.SuggestionsHelper;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * A wrapper class for {@link SuggestionsBuilder} that provides additional functionality for building suggestions.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public record SuggestionsBuilderWrapper(@NotNull SuggestionsBuilder builder) {

    /**
     * Adds suggestions to the list of completions.
     *
     * @param suggestions the suggestions to be added.
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
     * Adds suggestions to the list of completions with an associated tooltip.
     *
     * @param suggestions the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull Map<String, String> suggestions) {
        suggestions.forEach((suggestion, tooltip) -> builder.suggest(suggestion, MessageComponentSerializer.message().serializeOrNull(MiniMessage.miniMessage().deserializeOrNull(tooltip))));
        return this;
    }

    /**
     * Adds suggestions to the list of completions with an associated tooltip.
     *
     * @param suggestions the suggestions to be added.
     * @return this instance for method chaining.
     */
    @SafeVarargs
    @NotNull
    @CanIgnoreReturnValue
    public final SuggestionsBuilderWrapper suggest(@NotNull Map<String, Component>... suggestions) {
        Arrays.stream(suggestions)
                .forEach(map -> map.forEach((suggestion, tooltip) -> builder.suggest(suggestion, MessageComponentSerializer.message().serializeOrNull(tooltip))));
        return this;
    }

    /**
     * Adds multiple suggestions to the list of completions.
     *
     * @param suggestions the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull Iterable<String> suggestions) {
        suggestions.forEach(builder::suggest);
        return this;
    }

    /**
     * Adds multiple suggestions to the list of completions with an associated tooltip.
     *
     * @param suggestions the suggestions to be added.
     * @param tooltip     the tooltip to be shown with the suggestion.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull Iterable<Suggestion> suggestions, @NotNull String tooltip) {
        suggestions.forEach(suggestion -> builder.suggest(suggestion.text(), MessageComponentSerializer.message().serializeOrNull(MiniMessage.miniMessage().deserializeOrNull(tooltip))));
        return this;
    }

    /**
     * Adds multiple suggestions to the list of completions with an associated tooltip.
     *
     * @param suggestions the suggestions to be added.
     * @param tooltip     the tooltip to be shown with the suggestion.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull Iterable<Suggestion> suggestions, @NotNull Component tooltip) {
        suggestions.forEach(suggestion -> builder.suggest(suggestion.text(), MessageComponentSerializer.message().serializeOrNull(tooltip)));
        return this;
    }

    /**
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param suggestion the text to be suggested.
     * @param tooltip    the tooltip to be shown with the suggestion.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull String suggestion, @NotNull String tooltip) {
        builder.suggest(suggestion, MessageComponentSerializer.message().serializeOrNull(MiniMessage.miniMessage().deserializeOrNull(tooltip)));
        return this;
    }

    /**
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param suggestion the text to be suggested.
     * @param tooltip    the tooltip to be shown with the suggestion.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull String suggestion, @NotNull Component tooltip) {
        builder.suggest(suggestion, MessageComponentSerializer.message().serializeOrNull(tooltip));
        return this;
    }

    /**
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param suggestion the suggestion to be added.
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
     * Adds multiple suggestions to the list of completions.
     *
     * @param suggestions the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(@NotNull Suggestion... suggestions) {
        Arrays.stream(suggestions)
                .forEach(this::suggest);
        return this;
    }

    /**
     * Adds a suggestion to the list of completions.
     *
     * @param value the suggestion to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(int value) {
        builder.suggest(value);
        return this;
    }

    /**
     * Adds a suggestion to the list of completions with an associated tooltip.
     *
     * @param value   the suggestion to be added.
     * @param tooltip the tooltip to be shown with the suggestion.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggest(int value, @NotNull String tooltip) {
        builder.suggest(value, MessageComponentSerializer.message().serializeOrNull(MiniMessage.miniMessage().deserializeOrNull(tooltip)));
        return this;
    }

    /**
     * Adds list of suggestions to the list of completions if the value starts with current argument.
     * <p>
     * In simple words, if the user typed "he", and the suggestions are "hello", "hey", "hi", then only "hello" and "hey" will be suggested.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestIfValueStartsWithCurrent(@NotNull String... values) {
        return suggestIfValueStartsWithCurrent(Arrays.asList(values));
    }

    /**
     * Adds list of suggestions to the list of completions if the value starts with current argument.
     * <p>
     * In simple words, if the user typed "he", and the suggestions are "hello", "hey", "hi", then only "hello" and "hey" will be suggested.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestIfValueStartsWithCurrent(@NotNull Collection<String> values) {
        if (currentArg().isEmpty()) {
            values.forEach(builder::suggest);
            return this;
        }
        values.stream()
                .filter(value -> value.toLowerCase().startsWith(currentArgLowercase()))
                .forEach(builder::suggest);
        return this;
    }

    /**
     * Adds list of suggestions to the list of completions if the value starts with current argument.
     * <p>
     * In simple words, if the user typed "he", and the suggestions are "hello", "hey", "hi", then only "hello" and "hey" will be suggested.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestIfValueStartsWithCurrent(@NotNull Stream<String> values) {
        (currentArg().isEmpty() ? values : values.filter(v -> v.toLowerCase().startsWith(currentArgLowercase())))
                .forEach(builder::suggest);
        return this;
    }

    /**
     * Adds list of suggestions to the list of completions if the value starts with current argument.
     * <p>
     * In simple words, if the user typed "he", and the suggestions are "hello", "hey", "hi", then only "hello" and "hey" will be suggested.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestSuggestionIfValueStartsWithCurrent(@NotNull Collection<Suggestion> values) {
        if (currentArg().isEmpty()) {
            values.forEach(this::suggest);
            return this;
        }
        values.stream()
                .filter(value -> value.text().toLowerCase().startsWith(currentArgLowercase()))
                .forEach(this::suggest);
        return this;
    }

    /**
     * Adds list of suggestions to the list of completions if the value starts with current argument.
     * <p>
     * In simple words, if the user typed "he", and the suggestions are "hello", "hey", "hi", then only "hello" and "hey" will be suggested.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestSuggestionIfValueStartsWithCurrent(@NotNull Stream<Suggestion> values) {
        (currentArg().isEmpty() ? values : values.filter(v -> v.text().toLowerCase().startsWith(currentArgLowercase())))
                .forEach(this::suggest);
        return this;
    }

    /**
     * Returns the current argument that is being typed
     *
     * @return the current argument that is being typed
     */
    public String currentArg() {
        return builder.getRemaining();
    }

    /**
     * Returns the current argument that is being typed as lowercase.
     *
     * @return the current argument that is being typed as lowercase
     */
    public String currentArgLowercase() {
        return builder.getRemainingLowerCase();
    }

    /**
     * Returns the starting.
     *
     * @return the starting
     */
    public int start() {
        return builder.getStart();
    }

    /**
     * Returns the entire input text.
     *
     * @return the input text
     */
    public String input() {
        return builder.getInput();
    }

    /**
     * Returns the suggestions helper for this suggestions builder.
     *
     * @return the suggestions helper
     */
    @Deprecated(forRemoval = true)
    public SuggestionsHelper helper() {
        return new SuggestionsHelper(this);
    }

    /**
     * Returns a new {@link SuggestionsBuilderWrapper} with the specified starting.
     *
     * @param start the starting
     * @return a new {@link SuggestionsBuilderWrapper} with the specified starting
     */
    @NotNull
    public SuggestionsBuilderWrapper offset(int start) {
        return new SuggestionsBuilderWrapper(builder.createOffset(start));
    }

    /**
     * Returns an {@link CompletableFuture} containing an empty {@link Suggestions}.
     *
     * @return an {@link CompletableFuture} containing an empty {@link Suggestions}
     */
    @NotNull
    public static CompletableFuture<Suggestions> empty() {
        return Suggestions.empty();
    }

    /**
     * Returns an empty {@link Suggestions}.
     *
     * @return an empty {@link Suggestions}
     */
    @NotNull
    public static Suggestions emptyFuture() {
        return new Suggestions(StringRange.at(0), new ArrayList<>());
    }

    /**
     * Builds and returns the suggestions as a {@link Suggestions}.
     * This will usually only work if the call is from CompletableFuture.
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

    /**
     * Returns the underlying {@link SuggestionsBuilder}.
     * This would generally not be needed, but it is provided for convenience in-case you need to access the builder directly.
     *
     * @return the underlying {@link SuggestionsBuilder}
     */
    @Override
    @NotNull
    public SuggestionsBuilder builder() {
        return builder;
    }
}