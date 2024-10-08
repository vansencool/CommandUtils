package dev.vansen.commandutils.command;

import org.bukkit.entity.Player;

/**
 * An enumeration representing different types of checks that can be performed on the command sender.
 * These checks determine whether the command sender meets certain conditions.
 */
public enum CheckType {
    /**
     * Represents a check to ensure the sender is a {@link Player}.
     */
    PLAYER,

    /**
     * Represents a check to ensure the sender is the console.
     */
    CONSOLE,

    /**
     * Represents a check to ensure the sender is an entity (such as a mob).
     */
    ENTITY,

    /**
     * Represents a check to ensure the sender is a command block.
     */
    COMMAND_BLOCK,

    /**
     * Represents a check to ensure the sender is a proxied sender.
     */
    PROXIED_SENDER
}