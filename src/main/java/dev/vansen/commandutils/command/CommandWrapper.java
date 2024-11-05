package dev.vansen.commandutils.command;

import com.mojang.brigadier.context.CommandContext;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.messages.MessageTypes;
import dev.vansen.commandutils.sender.SenderTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A wrapper class for managing and accessing command context information within a command execution.
 * Provides utility methods to interact with command arguments, check the type of command sender,
 * and handle exceptions.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public record CommandWrapper(CommandContext<CommandSourceStack> context) {

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
     * Retrieves the {@link Player} with the given name.
     *
     * @param name the name of the player.
     * @return the player with the given name, can be null if the player does not exist.
     */
    @Nullable
    public Player player(@NotNull String name) {
        return Bukkit.getPlayer(name);
    }

    /**
     * Checks if a player with the given name exists.
     *
     * @param name the name of the player.
     * @return true if the player exists, false otherwise.
     */
    public boolean playerExists(@NotNull String name) {
        return Bukkit.getPlayer(name) != null;
    }

    /**
     * Retrieves the {@link OfflinePlayer} with the given name.
     *
     * @param name the name of the player.
     * @return the offline player with the given name
     */
    @NotNull
    public OfflinePlayer offlinePlayer(@NotNull String name) {
        return Bukkit.getOfflinePlayer(name);
    }

    /**
     * Sends a response to the command sender as a rich message.
     *
     * @param message the message to send
     */
    public void response(@Nullable String message) {
        if (message == null) return;
        sender().sendRichMessage(message);
    }

    /**
     * Sends a response to the command sender as a component message.
     *
     * @param message the message to send
     */
    public void response(@Nullable Component message) {
        if (message == null) return;
        sender().sendMessage(message);
    }

    /**
     * Sends multiple responses to the command sender as rich messages.
     *
     * @param messages the messages to send
     */
    public void response(@Nullable String... messages) {
        Arrays.stream(messages)
                .filter(Objects::nonNull)
                .forEach(message -> sender().sendRichMessage(message));
    }

    /**
     * Sends multiple responses to the command sender as component messages.
     *
     * @param messages the messages to send
     */
    public void response(@Nullable Component... messages) {
        Arrays.stream(messages)
                .filter(Objects::nonNull)
                .forEach(message -> sender().sendMessage(message));
    }

    /**
     * Sends a response to the command sender as a message type.
     *
     * @param message the message to send
     */
    public void response(@Nullable MessageTypes message) {
        if (message == null) return;
        switch (message.type()) {
            case MESSAGE -> sender().sendMessage(message.message());
            case ACTION_BAR -> sender().sendActionBar(MiniMessage.miniMessage().deserializeOrNull(message.message()));
        }
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
     * Retrieves a command argument by its name and converts it to the specified type.
     * If the argument does not exist, or inconvertible, the default value is returned.
     *
     * @param arg          the name of the argument.
     * @param clazz        the class of the argument type.
     * @param defaultValue the default value to return if the argument does not exist.
     * @param <T>          the type of the argument.
     * @return the argument value converted to the specified type or the default value if the argument does not exist.
     */
    @NotNull
    public <T> T arg(@NotNull String arg, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        try {
            return arg(arg, clazz);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Helper method to get arguments safely for the user.
     * Automatically parses the input to get the arguments in the command.
     *
     * @param index The index of the argument to retrieve (0-based), 0 is being the command itself (i.e. /example).
     * @return The argument at the given index, or an empty string if not available.
     */
    public String arg(int index) {
        String[] args = input().split(" ");
        if (index < args.length) {
            return args[index];
        }
        return ""; // Return empty if the argument does not exist
    }

    /**
     * Helper method to get arguments after a given index.
     * Automatically parses the input to get the arguments in the command.
     *
     * @param index The index of the argument to start retrieving from (0-based), 0 is being the command itself (i.e. /example).
     * @return A string containing the arguments after the given index, separated by spaces.
     */
    public String argsAfter(int index) {
        String[] args = input().split(" ");
        if (index < args.length) {
            return String.join(" ", Arrays.copyOfRange(args, index + 1, args.length));
        }
        return ""; // Return empty if the argument does not exist
    }

    /**
     * Helper method to check if a flag exists in the input.
     *
     * @param flag The flag to check.
     * @return True if the flag exists, false otherwise.
     */
    public boolean hasFlag(@NotNull String flag) {
        return Arrays.asList(context.getInput()
                        .split(" "))
                .remove(flag);
    }

    /**
     * Helper method to get the number of arguments.
     *
     * @return The number of arguments in the command.
     */
    public int argCount() {
        return input().split(" ").length;
    }

    /**
     * Helper method to get the number of arguments after a given index.
     *
     * @param index The index of the argument to start retrieving from (0-based), 0 is being the command itself (i.e. /example).
     * @return The number of arguments after the given index.
     */
    public int argCountAfter(int index) {
        String[] args = input().split(" ");
        if (index < args.length) {
            return args.length - index - 1;
        }
        return 0;
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
     * Retrieves a command argument by its name and converts it to a world.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a world.
     */
    public World argWorld(@NotNull String arg) {
        return context.getArgument(arg, World.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a game mode.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a game mode.
     */
    public GameMode argGameMode(@NotNull String arg) {
        return arg(arg, GameMode.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to an item stack.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to an item stack.
     */
    public ItemStack argItemStack(@NotNull String arg) {
        return context.getArgument(arg, ItemStack.class);
    }

    /**
     * Retrieves a command argument by its name and converts it to a player.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a player.
     */
    public Player argPlayer(@NotNull String arg) {
        return context.getArgument(arg, Player.class);
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
        return arg(arg, Boolean.class, def);
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
        return arg(arg, String.class, def);
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
        return arg(arg, Integer.class, def);
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
        return arg(arg, Double.class, def);
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
        return arg(arg, Float.class, def);
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
        return arg(arg, Long.class, def);
    }

    /**
     * Retrieves a command argument by its name and converts it to a world.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a world, or the default value if not present or invalid.
     */
    public World argWorld(@NotNull String arg, @Nullable World def) {
        try {
            return argWorld(arg);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to a game mode.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a game mode, or the default value if not present or invalid.
     */
    public GameMode argGameMode(@NotNull String arg, @Nullable GameMode def) {
        try {
            return argGameMode(arg);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to an item stack.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to an item stack, or the default value if not present or invalid.
     */
    public ItemStack argItemStack(@NotNull String arg, @Nullable ItemStack def) {
        try {
            return argItemStack(arg);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves a command argument by its name and converts it to a player.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a player, or the default value if not present or invalid.
     */
    public Player argPlayer(@NotNull String arg, @Nullable Player def) {
        try {
            return argPlayer(arg);
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
        return switch (senderType()) {
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
        return switch (senderType()) {
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
        return senderType() == SenderTypes.PLAYER;
    }

    /**
     * Checks if the sender is the console.
     *
     * @return true if the sender is the console, false otherwise.
     */
    public boolean isConsole() {
        return senderType() == SenderTypes.CONSOLE;
    }

    /**
     * Checks if the sender is an entity.
     *
     * @return true if the sender is an entity, false otherwise.
     */
    public boolean isEntity() {
        return senderType() == SenderTypes.ENTITY;
    }

    /**
     * Checks if the sender is a command block.
     *
     * @return true if the sender is a command block, false otherwise.
     */
    public boolean isBlock() {
        return senderType() == SenderTypes.COMMAND_BLOCK;
    }

    /**
     * Checks if the sender is a proxied command sender.
     *
     * @return true if the sender is a proxied command sender, false otherwise.
     */
    public boolean isProxied() {
        return senderType() == SenderTypes.PROXIED;
    }

    /**
     * Retrieves the sender type of the command sender.
     *
     * @return the sender type.
     */
    public SenderTypes senderType() {
        if (sender() instanceof Player) {
            return SenderTypes.PLAYER;
        }
        if (sender() instanceof ConsoleCommandSender) {
            return SenderTypes.CONSOLE;
        }
        if (sender() instanceof Entity) {
            return SenderTypes.ENTITY;
        }
        if (sender() instanceof BlockCommandSender) {
            return SenderTypes.COMMAND_BLOCK;
        }
        if (sender() instanceof ProxiedCommandSender) {
            return SenderTypes.PROXIED;
        }
        return SenderTypes.UNKNOWN;
    }

    /**
     * Throws a {@link CmdException} with the given message if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param message   the message to include in the exception
     * @throws CmdException if the predicate is true
     */
    public void throwIf(@NotNull Predicate<CommandWrapper> predicate, @NotNull String message) {
        if (predicate.test(this)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Throws a {@link CmdException} with the given message if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param message   the message to include in the exception
     * @throws CmdException if the predicate is false
     */
    public void throwIfNot(@NotNull Predicate<CommandWrapper> predicate, @NotNull String message) {
        if (!predicate.test(this)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Throws a {@link CmdException} with the given component message if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param message   the component message to include in the exception
     * @throws CmdException if the predicate is true
     */
    public void throwIf(@NotNull Predicate<CommandWrapper> predicate, @NotNull Component message) {
        if (predicate.test(this)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Throws a {@link CmdException} with the given component message if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param message   the component message to include in the exception
     * @throws CmdException if the predicate is false
     */
    public void throwIfNot(@NotNull Predicate<CommandWrapper> predicate, @NotNull Component message) {
        if (!predicate.test(this)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Throws a {@link CmdException} with the given runnable if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is true
     * @throws CmdException if the predicate is true
     */
    public void throwAndRunIf(@NotNull Predicate<CommandWrapper> predicate, @NotNull Runnable runnable) {
        if (predicate.test(this)) {
            runnable.run();
            check(c -> false);
        }
    }

    /**
     * Throws a {@link CmdException} with the given runnable if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is false
     * @throws CmdException if the predicate is false
     */
    public void throwAndRunIfNot(@NotNull Predicate<CommandWrapper> predicate, @NotNull Runnable runnable) {
        if (!predicate.test(this)) {
            runnable.run();
            check(c -> false);
        }
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
     * Runs the given runnable if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is true
     */
    public void runIf(@NotNull Predicate<CommandWrapper> predicate, @NotNull Runnable runnable) {
        if (predicate.test(this)) {
            runnable.run();
        }
    }

    /**
     * Runs the given runnable if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is false
     */
    public void runIfNot(@NotNull Predicate<CommandWrapper> predicate, @NotNull Runnable runnable) {
        if (!predicate.test(this)) {
            runnable.run();
        }
    }

    /**
     * Checks whether the command sender meets the specified condition.
     *
     * @param type the type of check to be performed.
     * @throws CmdException if the check fails.
     */
    public void check(@NotNull CheckType type) {
        Optional.of(type)
                .filter(t -> t == CheckType.PLAYER && !isPlayer())
                .ifPresent(t -> {
                    throw new CmdException(MessageTypes.PLAYER_EXCEPTION, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.CONSOLE && !isConsole())
                .ifPresent(t -> {
                    throw new CmdException(MessageTypes.CONSOLE_EXCEPTION, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.ENTITY && !isEntity())
                .ifPresent(t -> {
                    throw new CmdException(MessageTypes.ENTITY_EXCEPTION, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.COMMAND_BLOCK && !isBlock())
                .ifPresent(t -> {
                    throw new CmdException(MessageTypes.COMMAND_BLOCK_EXCEPTION, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.PROXIED_SENDER && !isProxied())
                .ifPresent(t -> {
                    throw new CmdException(MessageTypes.PROXIED_SENDER_EXCEPTION, sender());
                });
    }

    /**
     * Checks whether the command sender meets the specified condition and runs the given runnable if not.
     *
     * @param type the type of check to be performed.
     * @param task the runnable to run if the check fails.
     * @throws CmdException if the check fails.
     */
    public void check(@NotNull CheckType type, @NotNull Runnable task) {
        if (!(type == CheckType.PLAYER && isPlayer() ||
                type == CheckType.CONSOLE && isConsole() ||
                type == CheckType.ENTITY && isEntity() ||
                type == CheckType.COMMAND_BLOCK && isBlock() ||
                type == CheckType.PROXIED_SENDER && isProxied())) {
            task.run();
            check(c -> false);
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
        Optional.of(type)
                .filter(t -> t == CheckType.PLAYER && !isPlayer())
                .ifPresent(t -> {
                    throw new CmdException(message, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.CONSOLE && !isConsole())
                .ifPresent(t -> {
                    throw new CmdException(message, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.ENTITY && !isEntity())
                .ifPresent(t -> {
                    throw new CmdException(message, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.COMMAND_BLOCK && !isBlock())
                .ifPresent(t -> {
                    throw new CmdException(message, sender());
                });
        Optional.of(type)
                .filter(t -> t == CheckType.PROXIED_SENDER && !isProxied())
                .ifPresent(t -> {
                    throw new CmdException(message, sender());
                });
    }

    /**
     * Adds a custom check to the command.
     * If the check fails, a CmdException is thrown.
     *
     * @param check the custom check to add
     * @throws CmdException if the check fails
     */
    public void check(@NotNull Predicate<CommandWrapper> check) {
        if (!check.test(this)) {
            throw new CmdException((Component) null, null);
        }
    }

    /**
     * Adds a custom check to the command.
     * If the check fails, a CmdException is thrown.
     *
     * @param check   the custom check to add
     * @param message the custom message to be sent if the check fails
     * @throws CmdException if the check fails
     */
    public void check(@NotNull Predicate<CommandWrapper> check, @NotNull String message) {
        if (!check.test(this)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Adds a custom check to the command.
     * If the check fails, a CmdException is thrown.
     *
     * @param check   the custom check to add
     * @param message the custom message to be sent if the check fails
     * @throws CmdException if the check fails
     */
    public void check(@NotNull Predicate<CommandWrapper> check, @NotNull Component message) {
        if (!check.test(this)) {
            throw new CmdException(message, sender());
        }
    }

    /**
     * Adds a custom check to the command.
     * If the check fails, the specified task is executed.
     *
     * @param check the custom check to add
     * @param task  the task to execute if the check fails
     */
    public void check(@NotNull Predicate<CommandWrapper> check, @NotNull Runnable task) {
        if (!check.test(this)) {
            task.run();
            check(c -> false);
        }
    }

    /**
     * Checks whether the command sender is of the specified type.
     *
     * @param type the type of sender to check
     * @return true if the command sender is of the specified type, false otherwise
     */
    public boolean canExecute(@NotNull SenderTypes type) {
        return senderType() == type;
    }

    /**
     * Checks whether the command sender is of the specified types.
     *
     * @param types the types of sender to check
     * @return true if the command sender is of the specified types, false otherwise
     */
    public boolean canExecute(@NotNull SenderTypes... types) {
        return Arrays.stream(types)
                .anyMatch(this::canExecute);
    }

    /**
     * Checks whether the command sender is of the specified types.
     *
     * @param types the types of sender to check
     * @return true if the command sender is of the specified types, false otherwise
     */
    public boolean canExecute(@NotNull ExecutableSender types) {
        return canExecute(types.types());
    }

    /**
     * Retrieves the underlying {@link CommandContext} of the command.
     *
     * @return the command context of the command.
     */
    @Override
    @NotNull
    public CommandContext<CommandSourceStack> context() {
        return context;
    }
}