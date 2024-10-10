package dev.vansen.commandutils.sender;

import dev.vansen.commandutils.command.CommandWrapper;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class containing methods for retrieving sender information.
 */
@SuppressWarnings("unused")
public class SenderUtils {

    /**
     * Retrieves the plain sender type of the given context.
     *
     * @param context the context to retrieve the sender from.
     * @return the plain sender type, can be "player", "console", "entity", "command_block", "proxied_sender" or "unknown".
     */
    public String plainSender(@NotNull CommandWrapper context) {
        return switch (senderType(context.sender())) {
            case PLAYER -> "player";
            case CONSOLE -> "console";
            case ENTITY -> "entity";
            case COMMAND_BLOCK -> "command_block";
            case PROXIED -> "proxied_sender";
            default -> "unknown";
        };
    }

    /**
     * Retrieves the friendly sender type of the given context.
     *
     * @param context the context to retrieve the sender from.
     * @return the friendly sender type, can be "Player", "Console", "Entity", "Command Block", "Proxied Command Sender" or "Unknown".
     */
    public String friendlySender(@NotNull CommandWrapper context) {
        return switch (senderType(context.sender())) {
            case PLAYER -> "Player";
            case CONSOLE -> "Console";
            case ENTITY -> "Entity";
            case COMMAND_BLOCK -> "Command Block";
            case PROXIED -> "Proxied Command Sender";
            default -> "Unknown";
        };
    }

    /**
     * Checks if the sender is a player.
     *
     * @param context the context to retrieve the sender from.
     * @return true if the sender is a player, false otherwise.
     */
    public boolean isPlayer(@NotNull CommandWrapper context) {
        return senderType(context.sender()) == SenderTypes.PLAYER;
    }

    /**
     * Checks if the sender is the console.
     *
     * @param context the context to retrieve the sender from.
     * @return true if the sender is the console, false otherwise.
     */
    public boolean isConsole(@NotNull CommandWrapper context) {
        return senderType(context.sender()) == SenderTypes.CONSOLE;
    }

    /**
     * Checks if the sender is an entity.
     *
     * @param context the context to retrieve the sender from.
     * @return true if the sender is an entity, false otherwise.
     */
    public boolean isEntity(@NotNull CommandWrapper context) {
        return senderType(context.sender()) == SenderTypes.ENTITY;
    }

    /**
     * Checks if the sender is a command block.
     *
     * @param context the context to retrieve the sender from.
     * @return true if the sender is a command block, false otherwise.
     */
    public boolean isCommandBlock(@NotNull CommandWrapper context) {
        return senderType(context.sender()) == SenderTypes.COMMAND_BLOCK;
    }

    /**
     * Checks if the sender is a proxied command sender.
     *
     * @param context the context to retrieve the sender from.
     * @return true if the sender is a proxied command sender, false otherwise.
     */
    public boolean isProxied(@NotNull CommandWrapper context) {
        return senderType(context.sender()) == SenderTypes.PROXIED;
    }

    /**
     * Retrieves the sender type of the given sender.
     *
     * @param sender the sender to retrieve the type from.
     * @return the sender type.
     */
    public SenderTypes senderType(Object sender) {
        if (sender instanceof Player) {
            return SenderTypes.PLAYER;
        }
        if (sender instanceof ConsoleCommandSender) {
            return SenderTypes.CONSOLE;
        }
        if (sender instanceof Entity) {
            return SenderTypes.ENTITY;
        }
        if (sender instanceof BlockCommandSender) {
            return SenderTypes.COMMAND_BLOCK;
        }
        if (sender instanceof ProxiedCommandSender) {
            return SenderTypes.PROXIED;
        }
        return SenderTypes.UNKNOWN;
    }
}