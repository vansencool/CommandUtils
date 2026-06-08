package dev.vansen.commandutils.permission;

import dev.vansen.commandutils.command.BasicCommandDetails;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Represents a permission or requirement that determines whether
 * a command may be executed in a given context.
 *
 * <p>Permissions are composable and may be combined using logical
 * operations such as AND and OR.
 *
 * <p><b>Permission inheritance:</b><br>
 * Permissions apply to children as well. If a permission is set on
 * a command, subcommand, or argument, it will also apply to all of
 * its children (including subcommands, arguments, and executors).
 */
@SuppressWarnings("unused")
public final class CommandPermission {

    /**
     * A permission that only allows player senders, alternative to using playerExecute or checking manually.
     */
    public static final CommandPermission PLAYER_ONLY = single(req -> req.sender() instanceof Player);

    /**
     * A permission that only allows non-player (console) senders, alternative to using consoleExecute or checking manually.
     */
    public static final CommandPermission CONSOLE_ONLY = single(req -> !(req.sender() instanceof Player));

    /**
     * A permission that only allows operator (OP) senders.
     */
    public static final CommandPermission OP = single(req -> req.sender().isOp());

    /**
     * A permission that always allows execution.
     */
    public static final CommandPermission ALWAYS = single(req -> true);

    /**
     * A permission that never allows execution.
     */
    public static final CommandPermission NEVER = single(req -> false);

    private final Predicate<BasicCommandDetails> test;

    /**
     * Creates a new {@link CommandPermission} backed by the given predicate.
     *
     * @param test a predicate that evaluates the command requirement
     */
    private CommandPermission(@NotNull Predicate<BasicCommandDetails> test) {
        this.test = test;
    }

    /**
     * Evaluates whether this permission allows execution
     * in the given command context.
     *
     * @param requirement the command requirement context
     * @return {@code true} if execution is allowed
     */
    public boolean allows(@NotNull BasicCommandDetails requirement) {
        return test.test(requirement);
    }

    /**
     * Creates a permission that checks for a specific permission string.
     *
     * @param permission the permission to check, if it is null it will return {@link #ALWAYS}
     * @return a permission-based {@link CommandPermission}
     */
    @NotNull
    public static CommandPermission permission(@Nullable String permission) {
        if (permission == null) return ALWAYS;
        return new CommandPermission(req -> req.hasPermission(permission));
    }

    /**
     * Creates a permission that checks if the sender is in a specific world.
     * <p>
     * For this to work properly, it assumes the commands refresh everytime a player changes world. This can be done by listening to the PlayerChangedWorldEvent and refreshing the command for the player.
     *
     * @param worldName the name of the world to check
     * @return a world-based {@link CommandPermission}
     */
    @NotNull
    public static CommandPermission world(@NotNull String worldName) {
        return new CommandPermission(req -> {
            if (req.isPlayer()) {
                return req.player().getWorld().getName().equalsIgnoreCase(worldName);
            } else {
                return false;
            }
        });
    }

    /**
     * Creates a permission that checks if the sender is in any of the specified worlds.
     * <p>
     * For this to work properly, it assumes the commands refresh everytime a player changes world. This can be done by listening to the PlayerChangedWorldEvent and refreshing the command for the player.
     *
     * @param worldNames the names of the worlds to check
     * @return a world-based {@link CommandPermission}
     */
    @NotNull
    public static CommandPermission world(@NotNull String... worldNames) {
        return new CommandPermission(req -> {
            if (req.isPlayer()) {
                String playerWorld = req.player().getWorld().getName();
                for (String worldName : worldNames) {
                    if (playerWorld.equalsIgnoreCase(worldName)) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    /**
     * Creates a permission that negates another permission.
     *
     * @param permission the permission to negate
     * @return a negated {@link CommandPermission}
     */
    @NotNull
    public static CommandPermission not(@NotNull CommandPermission permission) {
        return new CommandPermission(req -> !permission.allows(req));
    }

    /**
     * Creates a permission that allows execution only if
     * all provided permissions allow it.
     *
     * @param permissions the permissions to combine
     * @return a combined AND permission
     */
    @NotNull
    public static CommandPermission and(@NotNull CommandPermission... permissions) {
        return new CommandPermission(req -> {
            for (CommandPermission permission : permissions) {
                if (!permission.allows(req)) {
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * Creates a permission that allows execution if
     * any provided permission allows it.
     *
     * @param permissions the permissions to combine
     * @return a combined OR permission
     */
    @NotNull
    public static CommandPermission or(@NotNull CommandPermission... permissions) {
        return new CommandPermission(req -> {
            for (CommandPermission permission : permissions) {
                if (permission.allows(req)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Creates a permission from a single requirement predicate.
     *
     * @param test the requirement predicate
     * @return a new {@link CommandPermission}
     */
    @NotNull
    public static CommandPermission single(@NotNull Predicate<BasicCommandDetails> test) {
        return new CommandPermission(test);
    }
}