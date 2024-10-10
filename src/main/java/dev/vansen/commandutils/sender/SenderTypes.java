package dev.vansen.commandutils.sender;

import org.bukkit.entity.Player;

/**
 * Represents the different types of senders that can execute a command.
 */
public enum SenderTypes {
    /**
     * Represents a {@link Player} sender.
     */
    PLAYER,

    /**
     * Represents a console sender.
     */
    CONSOLE,

    /**
     * Represents an entity (such as a mob) sender.
     */
    ENTITY,

    /**
     * Represents a command block sender.
     */
    COMMAND_BLOCK,

    /**
     * Represents a proxied command sender (such as a command block or other entity).
     */
    PROXIED,

    /**
     * Represents an unknown type of sender.
     */
    UNKNOWN
}