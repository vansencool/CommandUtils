package dev.vansen.commandutils.argument.arguments;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vansen.commandutils.argument.arguments.color.ArgumentColors;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A custom argument type for parsing color values.
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class ColorArgumentType implements CustomArgumentType.Converted<TextColor, String> {
    private final @NotNull String tooltip;
    private final @NotNull TextColor color;

    /**
     * Creates a new ColorArgumentType with a custom tooltip and color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @param color   The color of the tooltip.
     */
    public ColorArgumentType(@NotNull String tooltip, @NotNull TextColor color) {
        this.tooltip = tooltip;
        this.color = color;
    }

    /**
     * Creates a new ColorArgumentType with a custom tooltip and a default color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     */
    public ColorArgumentType(@NotNull String tooltip) {
        this(tooltip, TextColor.color(166, 233, 255));
    }

    /**
     * Creates a new ColorArgumentType with default tooltip ("Click to choose <color>") and color (166, 233, 255).
     */
    public ColorArgumentType() {
        this("Click to choose <color>");
    }

    /**
     * Returns a new ColorArgumentType with a default tooltip and color.
     *
     * @return A new ColorArgumentType instance.
     */
    public static @NotNull ColorArgumentType color() {
        return new ColorArgumentType();
    }

    /**
     * Returns a new ColorArgumentType with a custom tooltip.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @return A new ColorArgumentType instance.
     */
    public static @NotNull ColorArgumentType color(String tooltip) {
        return new ColorArgumentType(tooltip);
    }

    /**
     * Returns a new ColorArgumentType with a custom tooltip and color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @param color   The color of the tooltip.
     * @return A new ColorArgumentType instance.
     */
    public static @NotNull ColorArgumentType color(@NotNull String tooltip, @NotNull TextColor color) {
        return new ColorArgumentType(tooltip, color);
    }

    @Override
    public @NotNull TextColor convert(@NotNull String nativeType) throws CommandSyntaxException {
        try {
            if (ArgumentColors.colorNames.contains(nativeType.toLowerCase())) {
                return Objects.requireNonNull(TextColor.fromHexString(ArgumentColors.COLOR_MAP.get(nativeType)));
            } else {
                return Objects.requireNonNull(TextColor.fromHexString(nativeType));
            }
        } catch (@NotNull Exception e) {
            Message message = MessageComponentSerializer.message().serialize(Component.text("Invalid color, Double quote the hex code or use a color name!"));
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        try {
            ArgumentColors.colorNames.parallelStream()
                    .filter(name -> name.startsWith(builder.getInput().substring(builder.getInput().lastIndexOf(" ") + 1)))
                    .forEach(name -> builder.suggest(name, MessageComponentSerializer.message()
                            .serializeOrNull(MiniMessage.miniMessage().deserializeOrNull("<color:" + color.asHexString() + ">" + tooltip.replaceAll("<color>", name)))));
            return builder.buildFuture();
        } catch (@NotNull Exception e) {
            return Suggestions.empty();
        }
    }
}