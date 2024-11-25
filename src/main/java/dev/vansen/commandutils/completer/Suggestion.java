package dev.vansen.commandutils.completer;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a suggestion with text and optional tooltip information.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class Suggestion {

    /**
     * The text of the suggestion.
     */
    private final @NotNull String text;

    /**
     * The tooltip associated with the suggestion, if any.
     */
    private Message tooltip;

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
    public Suggestion(@NotNull String text, @NotNull String tooltip) {
        this.text = text;
        this.tooltip = MessageComponentSerializer.message().serializeOrNull(MiniMessage.miniMessage().deserializeOrNull(tooltip));
    }

    /**
     * Creates a new suggestion with the given text and tooltip.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     */
    public Suggestion(@NotNull String text, @NotNull Component tooltip) {
        this.text = text;
        this.tooltip = MessageComponentSerializer.message().serializeOrNull(tooltip);
    }

    /**
     * Creates a new suggestion with the given text and tooltip.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     */
    public Suggestion(@NotNull String text, @NotNull Message tooltip) {
        this.text = text;
        this.tooltip = tooltip;
    }

    /**
     * Factory method to create a new instance of {@link Suggestion}.
     *
     * @param text the text of the suggestion
     * @return a new {@link Suggestion} instance
     */
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
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
    @CanIgnoreReturnValue
    public Suggestion of(@NotNull String text, @NotNull Component tooltip) {
        return new Suggestion(text, tooltip);
    }

    /**
     * Factory method to create a new instance of {@link Suggestion}.
     *
     * @param text    the text of the suggestion
     * @param tooltip the tooltip associated with the suggestion
     * @return a new {@link Suggestion} instance
     */
    @CanIgnoreReturnValue
    public Suggestion of(@NotNull String text, @NotNull Message tooltip) {
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
     * Returns the tooltip associated with the suggestion, if any.
     *
     * @return the tooltip associated with the suggestion, or null if none
     */
    public @Nullable Message tooltip() {
        return tooltip;
    }
}