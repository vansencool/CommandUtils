package dev.vansen.commandutils.completer;

import com.mojang.brigadier.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

/**
 * A class that implements {@link Message} to provide a tooltip for tab completion suggestions.
 * This class is used to display additional information for a command suggestion.
 */
@SuppressWarnings("unused")
public final class Tooltiper implements Message {

    /**
     * The tooltip text to be displayed.
     */
    private final @NotNull String tooltip;

    /**
     * Constructs a new {@link Tooltiper} with the specified tooltip text.
     *
     * @param tooltip the text to be used as the tooltip. This should not be null.
     */
    public Tooltiper(@NotNull String tooltip) {
        this.tooltip = tooltip;
    }

    public Tooltiper(@NotNull Component tooltip) {
        this(PlainTextComponentSerializer.plainText().serialize(tooltip));
    }

    /**
     * Returns the tooltip text as a string.
     *
     * @return the tooltip text.
     */
    @Override
    public @NotNull String getString() {
        return tooltip;
    }

    /**
     * Creates a new {@link Tooltiper} instance with the specified tooltip text.
     *
     * @param tooltip the text to be used as the tooltip. This should not be null.
     * @return a new {@link Tooltiper} instance with the given tooltip text.
     */
    public static @NotNull Tooltiper of(@NotNull String tooltip) {
        return new Tooltiper(tooltip);
    }

    /**
     * Creates a new {@link Tooltiper} instance with the specified tooltip text.
     *
     * @param tooltip the text to be used as the tooltip. This should not be null.
     * @return a new {@link Tooltiper} instance with the given tooltip text.
     */
    public static @NotNull Tooltiper of(@NotNull Component tooltip) {
        return new Tooltiper(tooltip);
    }
}