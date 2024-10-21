package dev.vansen.commandutils.permission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a command permission that can either be a specific permission string or an operator (OP) level.
 * Command permissions determine what actions a user can perform based on their permissions or OP level.
 */
@SuppressWarnings("unused")
public final class CommandPermission {

    /**
     * A predefined {@link CommandPermission} representing operator (OP) permissions.
     */
    public static final CommandPermission OP = new CommandPermission(2);
    private final @Nullable String permission;
    private final int opLevel;

    /**
     * Constructs a {@link CommandPermission} with a specific permission string.
     * This type of permission is typically used for more granular control over command access.
     *
     * @param permission the permission string required to execute the command.
     */
    public CommandPermission(@NotNull String permission) {
        this.permission = permission;
        this.opLevel = -1;
    }

    /**
     * Constructs a {@link CommandPermission} with a specific operator (OP) level.
     * This type of permission is used to represent commands that are only accessible by users with a certain OP level.
     *
     * @param opLevel the OP level required to execute the command.
     */
    public CommandPermission(int opLevel) {
        this.permission = null;
        this.opLevel = opLevel;
    }

    /**
     * Factory method to create a {@link CommandPermission} based on a permission string.
     *
     * @param permission the permission string required to execute the command.
     * @return a new {@link CommandPermission} instance with the specified permission string.
     */
    @NotNull
    public static CommandPermission permission(@NotNull String permission) {
        return new CommandPermission(permission);
    }

    /**
     * Factory method to create a {@link CommandPermission} based on an operator (OP) level.
     *
     * @param opLevel the OP level required to execute the command.
     * @return a new {@link CommandPermission} instance with the specified operator (OP) level.
     */
    @NotNull
    public static CommandPermission op(int opLevel) {
        return new CommandPermission(opLevel);
    }

    /**
     * Gets the permission string associated with this {@link CommandPermission}.
     *
     * @return the permission string, or {@code null} if this permission is based on OP level.
     */
    @Nullable
    public String getPermission() {
        return permission;
    }

    /**
     * Gets the OP level associated with this {@link CommandPermission}.
     *
     * @return the OP level, or -1 if this permission is based on a permission string.
     */
    public int getOpLevel() {
        return opLevel;
    }

    /**
     * Determines if this {@link CommandPermission} is an OP-level permission.
     *
     * @return {@code true} if the permission is based on OP level; {@code false} otherwise.
     */
    public boolean isOpPermission() {
        return opLevel >= 0;
    }
}
