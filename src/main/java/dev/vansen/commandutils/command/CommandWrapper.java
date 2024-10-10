package dev.vansen.commandutils.command;

import com.mojang.brigadier.context.CommandContext;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.sender.SenderTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper class for managing and accessing command context information within a command execution.
 * Provides utility methods to interact with command arguments, check the type of command sender,
 * and handle exceptions.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class CommandWrapper {

    private final CommandContext<CommandSourceStack> context;

    /**
     * Constructs a new {@link CommandWrapper} with the given command context.
     *
     * @param context the command context to be wrapped. Must not be null.
     */
    public CommandWrapper(@NotNull CommandContext<CommandSourceStack> context) {
        this.context = context;
    }

    /**
     * Retrieves the {@link Player} who executed the command.
     *
     * @return the player who executed the command.
     * @throws ClassCastException if the sender is not an instance of {@link Player}.
     */
    @NotNull
    public Player player() {
        return (Player) context.getSource().getSender();
    }

    /**
     * Retrieves the {@link CommandSender} who executed the command.
     *
     * @return the sender of the command.
     */
    @NotNull
    public CommandSender sender() {
        return context.getSource().getSender();
    }

    /**
     * Retrieves the {@link Entity} who executed the command.
     *
     * @return the entity who executed the command.
     */
    @Nullable
    public Entity entity() {
        return context.getSource().getExecutor();
    }

    /**
     * Retrieves the {@link BlockCommandSender} who executed the command.
     *
     * @return the block command sender who executed the command, can be null if the sender is not a command block.
     */
    @Nullable
    public BlockCommandSender block() {
        return context.getSource().getExecutor() instanceof BlockCommandSender sender ? sender : null;
    }

    /**
     * Retrieves the {@link ProxiedCommandSender} who executed the command.
     *
     * @return the proxied command sender who executed the command, can be null if the sender is not a proxied command.
     */
    @Nullable
    public ProxiedCommandSender proxied() {
        return context.getSource().getExecutor() instanceof ProxiedCommandSender sender ? sender : null;
    }

    /**
     * Retrieves a command argument by its name and converts it to the specified type.
     *
     * @param arg   the name of the argument.
     * @param clazz the class of the argument type.
     * @param <T>   the type of the argument.
     * @return the argument value converted to the specified type.
     */
    @NotNull
    public <T> T arg(@NotNull String arg, @NotNull Class<T> clazz) {
        return context.getArgument(arg, clazz);
    }

    /**
     * Retrieves a command argument by its name and converts it to a boolean.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a boolean.
     */
    public boolean argBoolean(@NotNull String arg) {
        return context.getArgument(arg, Boolean.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a string.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a string.
     */
    public String argString(@NotNull String arg) {
        return context.getArgument(arg, String.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to an integer.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to an integer.
     */
    public int argInt(@NotNull String arg) {
        return context.getArgument(arg, Integer.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a double.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a double.
     */
    public double argDouble(@NotNull String arg) {
        return context.getArgument(arg, Double.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a float.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a float.
     */
    public float argFloat(@NotNull String arg) {
        return context.getArgument(arg, Float.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a long.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a long.
     */
    public long argLong(@NotNull String arg) {
        return context.getArgument(arg, Long.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a boolean.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a boolean, or the default value if not present or invalid.
     */
    public boolean argBoolean(@NotNull String arg, boolean def) {
        try {
            return context.getArgument(arg, Boolean.class);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to a string.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a string, or the default value if not present or invalid.
     */
    public String argString(@NotNull String arg, @NotNull String def) {
        try {
            return context.getArgument(arg, String.class);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to an integer.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to an integer, or the default value if not present or invalid.
     */
    public int argInt(@NotNull String arg, int def) {
        try {
            return context.getArgument(arg, Integer.class);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to a double.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a double, or the default value if not present or invalid.
     */
    public double argDouble(@NotNull String arg, double def) {
        try {
            return context.getArgument(arg, Double.class);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to a float.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a float, or the default value if not present or invalid.
     */
    public float argFloat(@NotNull String arg, float def) {
        try {
            return context.getArgument(arg, Float.class);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to a long.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a long, or the default value if not present or invalid.
     */
    public long argLong(@NotNull String arg, long def) {
        try {
            return context.getArgument(arg, Long.class);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves the plain sender type of the given context.
     *
     * @return the plain sender type, can be "player", "console", "entity", "command_block", "proxied_sender" or "unknown".
     */
    public String plainSender() {
        return switch (senderType(sender())) {
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
     * @return the friendly sender type, can be "Player", "Console", "Entity", "Command Block", "Proxied Command Sender" or "Unknown".
     */
    public String friendlySender() {
        return switch (senderType(sender())) {
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
     * @return true if the sender is a player, false otherwise.
     */
    public boolean isPlayer() {
        return senderType(sender()) == SenderTypes.PLAYER;
    }

    /**
     * Checks if the sender is the console.
     *
     * @return true if the sender is the console, false otherwise.
     */
    public boolean isConsole() {
        return senderType(sender()) == SenderTypes.CONSOLE;
    }

    /**
     * Checks if the sender is an entity.
     *
     * @return true if the sender is an entity, false otherwise.
     */
    public boolean isEntity() {
        return senderType(sender()) == SenderTypes.ENTITY;
    }

    /**
     * Checks if the sender is a command block.
     *
     * @return true if the sender is a command block, false otherwise.
     */
    public boolean isBlock() {
        return senderType(sender()) == SenderTypes.COMMAND_BLOCK;
    }

    /**
     * Checks if the sender is a proxied command sender.
     *
     * @return true if the sender is a proxied command sender, false otherwise.
     */
    public boolean isProxied() {
        return senderType(sender()) == SenderTypes.PROXIED;
    }

    /**
     * Retrieves the sender type of the given sender.
     *
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

    /**
     * Retrieves the entire input string of the command.
     *
     * @return the input string of the command.
     */
    @NotNull
    public String input() {
        return context.getInput();
    }

    /**
     * Checks whether the command sender meets the specified condition.
     *
     * @param type the type of check to be performed.
     * @throws CmdException if the check fails.
     */
    public void check(@NotNull CheckType type) {
        if (type == CheckType.PLAYER && !(sender() instanceof Player)) {
            throw new CmdException("<color:#ff4060>Exception: You must be a player to execute this command!</color>", sender());
        }
        if (type == CheckType.CONSOLE && !(sender() instanceof ConsoleCommandSender)) {
            throw new CmdException("<color:#ff4060>Exception: You must execute this command from the console!</color>", sender());
        }
        if (type == CheckType.ENTITY && !(sender() instanceof Entity)) {
            throw new CmdException("<color:#ff4060>Exception: You must be an entity to execute this command!</color>", sender());
        }
        if (type == CheckType.COMMAND_BLOCK && !(sender() instanceof BlockCommandSender)) {
            throw new CmdException("<color:#ff4060>Exception: This can only be executed by a command block!</color>", sender());
        }
        if (type == CheckType.PROXIED_SENDER && !(sender() instanceof ProxiedCommandSender)) {
            throw new CmdException("<color:#ff4060>Exception: You must be a proxied command sender to execute this command!</color>", sender());
        }
    }

    /**
     * Checks whether the command sender meets the specified condition and throws a custom exception if not.
     *
     * @param type    the type of check to be performed.
     * @param message the custom message to be sent if the check fails.
     * @throws CmdException if the check fails.
     */
    public void check(@NotNull CheckType type, @NotNull String message) {
        if (type == CheckType.PLAYER && !(sender() instanceof Player)) {
            throw new CmdException(message, sender());
        }
        if (type == CheckType.CONSOLE && sender() instanceof Player) {
            throw new CmdException(message, sender());
        }
        if (type == CheckType.ENTITY && !(sender() instanceof Entity)) {
            throw new CmdException(message, sender());
        }
        if (type == CheckType.COMMAND_BLOCK && !(sender() instanceof BlockCommandSender)) {
            throw new CmdException(message, sender());
        }
        if (type == CheckType.PROXIED_SENDER && !(sender() instanceof ProxiedCommandSender)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Retrieves the underlying {@link CommandContext} of the command.
     *
     * @return the command context of the command.
     */
    @NotNull
    public CommandContext<CommandSourceStack> context() {
        return context;
    }
}