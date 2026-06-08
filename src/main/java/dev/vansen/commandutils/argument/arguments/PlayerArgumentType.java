package dev.vansen.commandutils.argument.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.vansen.commandutils.argument.arguments.custom.CustomArgument;
import dev.vansen.commandutils.command.BasicCommandDetails;
import dev.vansen.commandutils.completer.Suggestion;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdSyntaxException;
import dev.vansen.commandutils.permission.CommandPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * A custom command argument that resolves an online {@link Player} by name.
 *
 * <p>
 * This argument provides:
 * </p>
 *
 * <ul>
 *   <li>Strict validation of player names (3–16 characters)</li>
 *   <li>Exact resolution of online players</li>
 *   <li>Tab-completion using {@link SuggestionsBuilderWrapper}</li>
 *   <li>Optional MiniMessage-based suggestion tooltips</li>
 *   <li>Runtime filtering of suggestions using {@link CommandPermission}</li>
 * </ul>
 *
 * <h2>Runtime requirement</h2>
 *
 * <p>
 * A {@link CommandPermission} may be assigned as a <b>runtime requirement</b>
 * using {@link #requirement(CommandPermission)}. When used in this context,
 * the permission is evaluated during tab-completion to determine whether a
 * suggested player should be visible to the command sender.
 * </p>
 *
 * <p>
 * Runtime requirements do <b>not</b> affect command execution.
 * They are only used for suggestion visibility.
 * </p>
 */
@SuppressWarnings("unused")
public final class PlayerArgumentType extends CustomArgument<Player, String> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * The MiniMessage tooltip used for suggestions.
     */
    private final @Nullable String tooltip;

    /**
     * Whether suggestion tooltips are enabled.
     */
    private boolean tooltipEnabled = true;

    /**
     * Runtime requirement used to filter visible suggestions.
     */
    private @NotNull CommandPermission requirement = CommandPermission.ALWAYS;

    /**
     * Creates a new {@link PlayerArgumentType} with a custom tooltip.
     *
     * @param tooltip the MiniMessage tooltip to use
     */
    public PlayerArgumentType(@Nullable String tooltip) {
        super(StringArgumentType.string());
        this.tooltip = tooltip;
    }

    /**
     * Creates a new {@link PlayerArgumentType} with the default tooltip.
     *
     * <p>
     * Default tooltip:
     * {@code <color:#a6e9ff>Click to choose <player></color>}
     * </p>
     */
    public PlayerArgumentType() {
        this("<color:#a6e9ff>Click to choose <player></color>");
    }

    /**
     * Creates a default {@link PlayerArgumentType}.
     *
     * @return a new player argument type
     */
    public static @NotNull PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    /**
     * Creates a {@link PlayerArgumentType} with a custom tooltip.
     *
     * @param tooltip the MiniMessage tooltip
     * @return a new player argument type
     */
    public static @NotNull PlayerArgumentType player(@NotNull String tooltip) {
        return new PlayerArgumentType(tooltip);
    }

    /**
     * Disables tooltips for suggestions.
     *
     * @return this instance
     */
    public @NotNull PlayerArgumentType withoutTooltip() {
        this.tooltipEnabled = false;
        return this;
    }

    /**
     * Enables tooltips for suggestions.
     *
     * @return this instance
     */
    public @NotNull PlayerArgumentType withTooltip() {
        this.tooltipEnabled = true;
        return this;
    }

    /**
     * Sets a runtime requirement that determines which players
     * are visible during tab completion.
     *
     * <p>
     * This reuses the {@link CommandPermission} system, even though the method is named "requirement".
     *
     * @param requirement the runtime requirement
     * @return this instance
     */
    public @NotNull PlayerArgumentType requirement(
            @NotNull CommandPermission requirement
    ) {
        this.requirement = requirement;
        return this;
    }

    /**
     * Parses and validates the provided player name.
     *
     * @param nativeValue the raw argument input
     * @return the resolved online player
     * @throws CmdSyntaxException if the name is invalid or the player is not online
     */
    @Override
    public @NotNull Player parseOrConvert(
            @NotNull String nativeValue
    ) throws CmdSyntaxException {

        int length = nativeValue.length();
        if (length < 3 || length > 16) {
            throw CmdSyntaxException.of(
                    Component.text(
                            "Player names must be between 3 and 16 characters."
                    )
            );
        }

        Player player = Bukkit.getPlayerExact(nativeValue);
        if (player == null) {
            throw CmdSyntaxException.of(
                    MINI_MESSAGE.deserialize(
                            "<color:#ff576d>Invalid player <player></color>"
                                    .replace("<player>", nativeValue)
                    )
            );
        }

        return player;
    }

    /**
     * Provides tab-completion suggestions for online players.
     *
     * @param context the command context details
     * @param wrapper the suggestions builder wrapper
     * @return a future containing the suggestions
     */
    @Override
    public @NotNull CompletableFuture<Suggestions> suggest(
            @NotNull BasicCommandDetails context,
            @NotNull SuggestionsBuilderWrapper wrapper
    ) {
        Stream<Suggestion> suggestions = Bukkit.getOnlinePlayers()
                .stream()
                .filter(player ->
                        requirement.allows(context)
                )
                .map(Player::getName)
                .map(name -> {
                    if (!tooltipEnabled || tooltip == null) {
                        return new Suggestion(name);
                    }

                    return new Suggestion(
                            name,
                            MINI_MESSAGE.deserialize(
                                    tooltip.replace("<player>", name)
                            )
                    );
                });

        wrapper.suggestSuggestionIfValueStartsWithCurrent(suggestions);
        return wrapper.builder().buildFuture();
    }
}
