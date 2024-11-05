package dev.vansen.commandutils.completer;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a suggestion with text and optional tooltip information.
 */
@SuppressWarnings("unused")
public final class Suggestion {

    /**
     * The text of the suggestion.
     */
    private @NotNull String text;

    /**
     * The tooltip associated with the suggestion, if any.
     */
    private Tooltiper tooltip;

    /**
     * Creates a new suggestion with the given text.
     *
     * @param text the text of the suggestion
     */
    public Suggestion(@NotNull String text) {
        this.text = text;
    }

    /**
     * Creates a new suggestion with the given text and tooltip.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     */
    public Suggestion(@NotNull String text, @NotNull Tooltiper tooltip) {
        this.text = text;
        this.tooltip = tooltip;
    }

    /**
     * Creates a new suggestion with the given text and tooltip.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     */
    public Suggestion(@NotNull String text, @NotNull String tooltip) {
        this.text = text;
        this.tooltip = Tooltiper.of(tooltip);
    }

    /**
     * Creates a new suggestion with the given text and tooltip.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     */
    public Suggestion(@NotNull String text, @NotNull Component tooltip) {
        this.text = text;
        this.tooltip = Tooltiper.of(tooltip);
    }

    /**
     * Factory method to create a new instance of {@link Suggestion}.
     *
     * @param text the text of the suggestion
     * @return a new {@link Suggestion} instance
     */
    public Suggestion of(@NotNull String text) {
        return new Suggestion(text);
    }

    /**
     * Factory method to create a new instance of {@link Suggestion}.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     * @return a new {@link Suggestion} instance
     */
    public Suggestion of(@NotNull String text, @NotNull Tooltiper tooltip) {
        return new Suggestion(text, tooltip);
    }

    /**
     * Factory method to create a new instance of {@link Suggestion}.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     * @return a new {@link Suggestion} instance
     */
    public Suggestion of(@NotNull String text, @NotNull String tooltip) {
        return new Suggestion(text, tooltip);
    }

    /**
     * Factory method to create a new instance of {@link Suggestion}.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     * @return a new {@link Suggestion} instance
     */
    public Suggestion of(@NotNull String text, @NotNull Component tooltip) {
        return new Suggestion(text, tooltip);
    }

    /**
     * Returns the text of the suggestion.
     *
     * @return the text of the suggestion
     */
    public @NotNull String text() {
        return text;
    }

    /**
     * Sets the text of the suggestion and returns the updated suggestion object.
     *
     * @param text the new text of the suggestion
     * @return the updated suggestion object
     */
    public Suggestion text(@NotNull String text) {
        this.text = text;
        return this;
    }

    /**
     * Returns the tooltip associated with the suggestion, if any.
     *
     * @return the tooltip associated with the suggestion, or null if none
     */
    public Tooltiper tooltip() {
        return tooltip;
    }
}