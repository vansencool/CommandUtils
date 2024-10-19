package dev.vansen.commandutils.messages;

import org.jetbrains.annotations.Nullable;

public class SystemMessages {
    /**
     * Exception message for when a player is required to execute a command.
     */
    public static @Nullable String PLAYER_EXCEPTION = "<color:#ff4060>Exception: You must be a player to execute this command!</color>";

    /**
     * Exception message for when the console is required to execute a command.
     */
    public static @Nullable String CONSOLE_EXCEPTION = "<color:#ff4060>Exception: You must execute this command from the console!</color>";

    /**
     * Exception message for when an entity is required to execute a command.
     */
    public static @Nullable String ENTITY_EXCEPTION = "<color:#ff4060>Exception: You must be an entity to execute this command!</color>";

    /**
     * Exception message for when a command block is required to execute a command.
     */
    public static @Nullable String COMMAND_BLOCK_EXCEPTION = "<color:#ff4060>Exception: This can only be executed by a command block!</color>";

    /**
     * Exception message for when a proxied command sender is required to execute a command.
     */
    public static @Nullable String PROXIED_SENDER_EXCEPTION = "<color:#ff4060>Exception: You must be a proxied command sender to execute this command!</color>";

    /**
     * Exception message for when a player is not allowed to execute a command.
     */
    public static @Nullable String NOT_ALLOWED_PLAYER = "<color:#ff4060>You are not allowed to execute this as a player!</color>";

    /**
     * Exception message for when the console is not allowed to execute a command.
     */
    public static @Nullable String NOT_ALLOWED_CONSOLE = "<color:#ff4060>You are not allowed to execute this from the console!</color>";

    /**
     * Exception message for when an entity is not allowed to execute a command.
     */
    public static @Nullable String NOT_ALLOWED_ENTITY = "<color:#ff4060>You are not allowed to execute this as an entity!</color>";

    /**
     * Exception message for when a command block is not allowed to execute a command.
     */
    public static @Nullable String NOT_ALLOWED_COMMAND_BLOCK = "<color:#ff4060>You are not allowed to execute this from a command block!</color>";

    /**
     * Exception message for when a proxied command sender is not allowed to execute a command.
     */
    public static @Nullable String NOT_ALLOWED_PROXIED_SENDER = "<color:#ff4060>You are not allowed to execute this as a proxied command sender!</color>";

    // Private constructor
    private SystemMessages() {
    }
}
