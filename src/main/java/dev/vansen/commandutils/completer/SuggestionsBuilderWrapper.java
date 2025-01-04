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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

/**
 * A wrapper class for {@link SuggestionsBuilder} to simplify and enhance the process of
 * building tab completion suggestions.
 * This class provides methods to add suggestions and build the completion results.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public record SuggestionsBuilderWrapper(@NotNull SuggestionsBuilder builder) {

    /**
     * Constructs a new {@link SuggestionsBuilderWrapper} with the specified {@link SuggestionsBuilder}.
     *
     * @param builder the {@link SuggestionsBuilder} instance to wrap. This should not be null.
     */
    public SuggestionsBuilderWrapper {
    }

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
     * Adds list of suggestions to the list of completions if the current argument is equal to the specified value, or the current argument is empty.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestIfCurrentStartsWith(@NotNull Iterable<String> values) {
        if (helper().currentArgOr().isEmpty()) {
            values.forEach(builder::suggest);
            return this;
        }
        StreamSupport.stream(values.spliterator(), true)
                .filter(this::startsWithCurrent)
                .forEach(builder::suggest);
        return this;
    }

    /**
     * Adds list of suggestions to the list of completions if the current argument is equal to the specified value, or the current argument is empty.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestIfCurrentStartsWith(@NotNull String... values) {
        return suggestIfCurrentStartsWith(Arrays.asList(values));
    }

    /**
     * Adds list of suggestions to the list of completions if the current argument is equal to the specified value, or the current argument is empty.
     *
     * @param values the suggestions to be added.
     * @return this instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SuggestionsBuilderWrapper suggestIfCurrentStartsWith(@NotNull Iterable<String> values, @NotNull String tooltip) {
        if (helper().currentArgOr().isEmpty()) {
            values.forEach(builder::suggest);
            return this;
        }
        StreamSupport.stream(values.spliterator(), true)
                .filter(this::startsWithCurrent)
                .forEach(suggestion -> suggest(suggestion, tooltip));
        return this;
    }

    /**
     * Checks if the specified value is the current argument or the current argument is empty.
     *
     * @param value the value to check
     * @return true if the value is the current argument or the current argument is empty
     */
    public boolean isCurrent(@NotNull String value) {
        return equalsOrEmpty(helper().currentArgOr(), value);
    }

    /**
     * Checks if the specified value starts with the current argument or the current argument is empty.
     *
     * @param value the value to check
     * @return true if the value starts with the current argument or the current argument is empty
     */
    public boolean startsWithCurrent(@NotNull String value) {
        return startsWithOrEmpty(helper().currentArgOr(), value);
    }

    /**
     * Checks if the specified value equals the check value or the value is empty.
     *
     * @param value the value to check
     * @param check the check value
     * @return true if the value equals the check value or the value is empty
     */
    public boolean equalsOrEmpty(@NotNull String value, @NotNull String check) {
        return value.equals(check) || value.isEmpty();
    }

    /**
     * Checks if the specified value starts with the check value or the value is empty.
     *
     * @param value the value to check
     * @param check the check value
     * @return true if the value starts with the check value or the value is empty
     */
    public boolean startsWithOrEmpty(@NotNull String value, @NotNull String check) {
        return value.startsWith(check) || value.isEmpty();
    }

    /**
     * Checks if the specified value starts with the check value and is not empty.
     *
     * @param value the value to check
     * @param check the check value
     * @return true if the value starts with the check value and is not empty
     */
    public boolean startsWithNotEmpty(@NotNull String value, @NotNull String check) {
        return value.startsWith(check) && !value.isEmpty();
    }

    /**
     * Returns the starting position of the suggestions.
     *
     * @return the starting position of the suggestions
     */
    public int start() {
        return builder.getStart();
    }

    /**
     * Returns the remaining text.
     *
     * @return the remaining text
     */
    public String remaining() {
        return builder.getRemaining();
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
    public SuggestionsHelper helper() {
        return new SuggestionsHelper(this);
    }

    /**
     * Returns a new {@link SuggestionsBuilderWrapper} with the specified starting position.
     *
     * @param start the starting position
     * @return a new {@link SuggestionsBuilderWrapper} with the specified starting position
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
    public CompletableFuture<Suggestions> empty() {
        return Suggestions.empty();
    }

    /**
     * Returns an empty {@link Suggestions}.
     *
     * @return an empty {@link Suggestions}
     */
    @NotNull
    public Suggestions emptyFuture() {
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
     * This would generally not be needed, but it is provided for convenience in-case you need to access the builder directly (likely for advanced usage).
     *
     * @return the underlying {@link SuggestionsBuilder}
     */
    @Override
    @NotNull
    public SuggestionsBuilder builder() {
        return builder;
    }
}