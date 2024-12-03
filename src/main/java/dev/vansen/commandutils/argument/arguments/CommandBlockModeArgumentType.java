package dev.vansen.commandutils.argument.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class CommandBlockModeArgumentType implements CustomArgumentType<String, String> {
    private final @NotNull String tooltip;
    private final @NotNull TextColor color;
    private boolean haveTooltip = true;
    private boolean ignoreCase = true;
    private boolean giveAsLowerCase = true;

    /**
     * Creates a new CommandBlockModeArgumentType with a custom tooltip and color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @param color   The color of the tooltip.
     */
    public CommandBlockModeArgumentType(@NotNull String tooltip, @NotNull TextColor color) {
        this.tooltip = tooltip;
        this.color = color;
    }

    /**
     * Creates a new CommandBlockModeArgumentType with a custom tooltip and a default color (166, 233, 255).
     *
     * @param tooltip The tooltip to display when providing suggestions.
     */
    public CommandBlockModeArgumentType(@NotNull String tooltip) {
        this(tooltip, TextColor.color(166, 233, 255));
    }

    /**
     * Creates a new CommandBlockModeArgumentType with default tooltip ("Click to choose <color>") and color (166, 233, 255).
     */
    public CommandBlockModeArgumentType() {
        this("Click to choose <color>");
    }

    /**
     * Returns a new CommandBlockModeArgumentType with a default tooltip and color.
     *
     * @return A new CommandBlockModeArgumentType instance.
     */
    public static @NotNull CommandBlockModeArgumentType mode() {
        return new CommandBlockModeArgumentType();
    }

    /**
     * Returns a new CommandBlockModeArgumentType with a custom tooltip.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @return A new CommandBlockModeArgumentType instance.
     */
    public static @NotNull CommandBlockModeArgumentType mode(String tooltip) {
        return new CommandBlockModeArgumentType(tooltip);
    }

    /**
     * Returns a new CommandBlockModeArgumentType with a custom tooltip and color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @param color   The color of the tooltip.
     * @return A new CommandBlockModeArgumentType instance.
     */
    public static @NotNull CommandBlockModeArgumentType mode(@NotNull String tooltip, @NotNull TextColor color) {
        return new CommandBlockModeArgumentType(tooltip, color);
    }

    /**
     * Returns whether the player argument type has a tooltip.
     *
     * @return True if the player argument type has a tooltip, false otherwise.
     */
    public boolean hasTooltip() {
        return haveTooltip;
    }

    /**
     * Sets the suggestions to not have a tooltip.
     *
     * @return The current CommandBlockModeArgumentType instance.
     */
    public CommandBlockModeArgumentType withoutTooltip() {
        haveTooltip = false;
        return this;
    }

    /**
     * Sets the suggestions to have a tooltip.
     *
     * @return The current CommandBlockModeArgumentType instance.
     */
    public CommandBlockModeArgumentType withTooltip() {
        haveTooltip = true;
        return this;
    }

    /**
     * Sets the parser to ignore case, i.e. only "chain" is accepted, doing "Chain" or anything won't work.
     *
     * @return The current CommandBlockModeArgumentType instance.
     */
    public CommandBlockModeArgumentType doNotIgnoreCase() {
        ignoreCase = false;
        return this;
    }

    /**
     * Sets the parser to ignore case, i.e. "chain" and "Chain" are both valid.
     *
     * @return The current CommandBlockModeArgumentType instance.
     */
    public CommandBlockModeArgumentType ignoreCase() {
        ignoreCase = true;
        return this;
    }

    /**
     * Sets the parser to give the input as normal, i.e. "Chain" is given as "Chain" instead of "chain".
     *
     * @return The current CommandBlockModeArgumentType instance.
     */
    public CommandBlockModeArgumentType asNormal() {
        giveAsLowerCase = false;
        return this;
    }

    /**
     * Sets the parser to give the input as lowercase, i.e. "Chain" is given as "chain" instead of "Chain".
     *
     * @return The current CommandBlockModeArgumentType instance.
     */
    public CommandBlockModeArgumentType asLowercase() {
        giveAsLowerCase = true;
        return this;
    }

    @Override
    public @NotNull String parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readString();
        if (ignoreCase) input = input.toLowerCase();
        if (!input.equals("chain") && !input.equals("repeat") && !input.equals("impulse")) {
            Message message = MessageComponentSerializer.message().serialize(Component.text("Invalid command block mode! Valid modes: chain, repeat, impulse"));
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
        if (giveAsLowerCase) input = input.toLowerCase();
        return giveAsLowerCase ? input : reader.readString();
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(final @NotNull CommandContext<S> context, final @NotNull SuggestionsBuilder builder) {
        if (!haveTooltip) {
            Stream.of("chain", "repeat", "impulse")
                    .filter(mode -> mode.startsWith(builder.getInput().substring(builder.getInput().lastIndexOf(" ") + 1)))
                    .forEach(builder::suggest);
            return builder.buildFuture();
        }
        Stream.of("chain", "repeat", "impulse")
                .filter(mode -> mode.startsWith(builder.getInput().substring(builder.getInput().lastIndexOf(" ") + 1)))
                .forEach(mode -> builder.suggest(mode, MessageComponentSerializer.message().serialize(Component.text(tooltip.replaceAll("<mode>", mode))
                        .color(color))));
        return builder.buildFuture();
    }
}
