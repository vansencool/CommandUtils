package dev.vansen.commandutils.completer;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a suggestion with text and optional tooltip information.
 */
@SuppressWarnings("unused")
public class Suggestion {

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