package dev.vansen.commandutils.argument.arguments;

import com.mojang.brigadier.Message;
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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A custom argument type for parsing player names.
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class PlayerArgumentType implements CustomArgumentType.Converted<Player, String> {
    private String tooltip;
    private @Nullable TextColor color;

    /**
     * Creates a new PlayerArgumentType with a custom tooltip and color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @param color   The color of the tooltip.
     */
    public PlayerArgumentType(@NotNull String tooltip, @Nullable TextColor color) {
        this.tooltip = tooltip;
        this.color = color;
    }

    /**
     * Creates a new PlayerArgumentType with a custom tooltip and a default color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     */
    public PlayerArgumentType(@NotNull String tooltip) {
        this(tooltip, TextColor.color(166, 233, 255));
    }

    /**
     * Creates a new PlayerArgumentType with no tooltip.
     */
    public PlayerArgumentType() {
    }

    /**
     * Returns a new PlayerArgumentType with a default tooltip and color.
     *
     * @return A new PlayerArgumentType instance.
     */
    public static @NotNull PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    /**
     * Returns a new PlayerArgumentType with a custom tooltip.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @return A new PlayerArgumentType instance.
     */
    public static @NotNull PlayerArgumentType player(String tooltip) {
        return new PlayerArgumentType(tooltip);
    }

    /**
     * Returns a new PlayerArgumentType with a custom tooltip and color.
     *
     * @param tooltip The tooltip to display when providing suggestions.
     * @param color   The color of the tooltip.
     * @return A new PlayerArgumentType instance.
     */
    public static @NotNull PlayerArgumentType player(@NotNull String tooltip, @Nullable TextColor color) {
        return new PlayerArgumentType(tooltip, color);
    }

    @Override
    public @NotNull Player convert(@NotNull String nativeType) throws CommandSyntaxException {
        if (nativeType.length() < 3) {
            Message message = MessageComponentSerializer.message().serialize(Component
                    .text("Too short player name!"));
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
        if (nativeType.length() > 16) {
            Message message = MessageComponentSerializer.message().serialize(Component
                    .text("Too long player name!"));
            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }
        if (Bukkit.getPlayerExact(nativeType) == null) {
            Message message = MessageComponentSerializer.message().serialize(Component.text("Invalid player ")
                    .append(Component.text(
                            nativeType + "!"
                    ))
                    .color(TextColor.fromHexString("#ff576d")));

            throw new CommandSyntaxException(new SimpleCommandExceptionType(message), message);
        }

        return Objects.requireNonNull(Bukkit.getPlayerExact(nativeType));
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        Bukkit.getOnlinePlayers()
                .parallelStream()
                .forEach(player -> {
                    if (tooltip == null) builder.suggest(player.getName());
                    else builder.suggest(player.getName(), MessageComponentSerializer.message()
                            .serialize(Component.text(tooltip.replaceAll("<player>", player.getName()))
                                    .color(color)));
                });
        return builder.buildFuture();
    }
}