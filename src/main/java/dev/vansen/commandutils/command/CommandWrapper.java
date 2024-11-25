package dev.vansen.commandutils.command;

import com.mojang.brigadier.context.CommandContext;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.messages.MessageTypes;
import dev.vansen.commandutils.sender.SenderTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Retrieves the {@link ConsoleCommandSender} who executed the command.
     *
     * @return the console command sender who executed the command, can be null if the sender is not a console.
     */
    @Nullable
    public ConsoleCommandSender console() {
        return context.getSource().getSender() instanceof ConsoleCommandSender sender ? sender : null;
    }

    /**
     * Retrieves the {@link RemoteConsoleCommandSender} who executed the command.
     *
     * @return the remote console command sender who executed the command, can be null if the sender is not a remote console.
     */
    @Nullable
    public RemoteConsoleCommandSender remoteConsole() {
        return context.getSource().getSender() instanceof RemoteConsoleCommandSender sender ? sender : null;
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
            case MESSAGE -> sender().sendRichMessage(message.message());
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
     * Helper method to get input as a list.
     *
     * @return A list of strings containing the input in the command, separated by spaces.
     */
    public List<String> inputAsList() {
        return Arrays.asList(input().split(" "));
    }

    /**
     * Helper method to get input as an array.
     *
     * @return An array of strings containing the input in the command, separated by spaces.
     */
    public String[] inputAsArray() {
        return input().split(" ");
    }

    /**
     * Helper method to get input as a stream.
     *
     * @return A stream of strings containing the input in the command, separated by spaces.
     */
    public Stream<String> inputAsStream() {
        return Arrays.stream(input().split(" "));
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
     * Helper method to check if a flag exists multiple times in the input.
     *
     * @param flag The flag to check.
     * @return True if the flag exists multiple times, false otherwise.
     */
    public boolean hasFlagMultipleTimes(@NotNull String flag) {
        AtomicInteger count = new AtomicInteger();
        inputAsStream()
                .forEach(arg -> {
                    if (arg.equals(flag)) count.incrementAndGet();
                });
        return count.get() > 1;
    }

    /**
     * Helper method to get the number of times a flag exists in the input.
     *
     * @param flag The flag to check.
     * @return The number of times the flag exists in the input.
     */
    public int numberOfFlags(@NotNull String flag) {
        AtomicInteger count = new AtomicInteger();
        inputAsStream()
                .forEach(arg -> {
                    if (arg.equals(flag)) count.incrementAndGet();
                });
        return count.get();
    }

    /**
     * Helper method to get the value of a flag, like "/example --flag some_value".
     *
     * @param flag The flag to get the value of.
     * @return The value of the flag, or an empty string if the flag does not exist.
     */
    public String parameterForFlag(@NotNull String flag) {
        String[] args = input().split(" ");
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flag)) {
                return args[i + 1];
            }
        }
        return "";
    }

    /**
     * Helper method to check if a flag exists in the input.
     *
     * @param flag The flag to check.
     * @return True if the flag exists, false otherwise.
     */
    public boolean hasFlags(@NotNull String flag) {
        return inputAsStream()
                .anyMatch(arg -> arg.startsWith(flag));
    }

    /**
     * Helper method to check if multiple flags exist in the input.
     *
     * @param flags The flags to check.
     * @return True if all flags exist, false otherwise.
     */
    public boolean hasFlags(@NotNull String... flags) {
        return Arrays.stream(flags)
                .anyMatch(this::hasFlags);
    }

    /**
     * Helper method to check if multiple flags exist in the input.
     *
     * @param flags The flags to check.
     * @return True if all flags exist, false otherwise.
     */
    public boolean hasFlags(@NotNull Collection<String> flags) {
        return flags.stream()
                .anyMatch(this::hasFlags);
    }

    /**
     * Helper method to check if any flag exists in the input that starts with a hyphen or double hyphen (- or --).
     *
     * @return True if any flag exists, false otherwise.
     */
    public boolean hasFlags() {
        return inputAsStream()
                .anyMatch(arg -> arg.startsWith("--") || arg.startsWith("-"));
    }

    /**
     * Helper method to get all flags starting with any of the given prefixes.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return A list of strings containing the flags starting with the given prefix.
     */
    public List<String> flagsStarting(@NotNull String... prefixes) {
        return inputAsStream()
                .filter(arg -> Arrays.stream(prefixes)
                        .anyMatch(arg::startsWith))
                .toList();
    }

    /**
     * Helper method to get all flags ending with a given suffix.
     *
     * @param suffix The suffix of the flags to retrieve.
     * @return A list of strings containing the flags ending with the given suffix.
     */
    public List<String> flagsEnding(@NotNull String suffix) {
        return inputAsStream()
                .filter(arg -> arg.endsWith(suffix))
                .toList();
    }

    /**
     * Helper method to get all flags containing a given substring.
     *
     * @param substring The substring of the flags to retrieve.
     * @return A list of strings containing the flags containing the given substring.
     */
    public List<String> flagsContaining(@NotNull String substring) {
        return inputAsStream()
                .filter(arg -> arg.contains(substring))
                .collect(Collectors.toList());
    }

    /**
     * Helper method to get the input without flags that starts with a hyphen or double hyphen (- or --).
     *
     * @return A string containing the input without flags.
     */
    public String inputWithoutFlags() {
        return String.join(" ", inputAsStream()
                .filter(arg -> !arg.startsWith("--") && !arg.startsWith("-"))
                .toArray(String[]::new));
    }

    /**
     * Helper method to get the input without flags that starts with any of the given prefixes.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return A string containing the input without flags.
     */
    public String inputWithoutFlags(@NotNull String... prefixes) {
        return String.join(" ", inputAsStream()
                .filter(arg -> Arrays.stream(prefixes).noneMatch(arg::startsWith))
                .toArray(String[]::new));
    }

    /**
     * Helper method to get the input without flags that starts with any of the given prefixes.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return A string containing the input without flags.
     */
    public String inputWithoutFlags(@NotNull Collection<String> prefixes) {
        return inputWithoutFlags(prefixes.toArray(new String[0]));
    }

    /**
     * Helper method to get the input without the command itself.
     *
     * @return A string containing the input without the command itself.
     */
    public String inputWithoutCommand() {
        return String.join(" ", inputAsStream()
                .skip(1)
                .toArray(String[]::new));
    }

    /**
     * Helper method to get the number of flags.
     *
     * @return The number of flags in the command.
     */
    public int flagCount() {
        return flagsStarting("--").size() + flagsStarting("-").size();
    }

    /**
     * Helper method to get the number of flags starting with a given prefix.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return The number of flags starting with the given prefix.
     */
    public int flagCount(@NotNull String... prefixes) {
        return flagsStarting(prefixes).size();
    }

    /**
     * Helper method to get the first flag.
     *
     * @return The first flag in the command, or an empty string if not found.
     */
    public String firstFlag() {
        try {
            return flagsStarting("--")
                    .getFirst();
        } catch (@NotNull NoSuchElementException e) {
            try {
                return flagsStarting("-")
                        .getFirst();
            } catch (@NotNull NoSuchElementException ignored) {
                return "";
            }
        }
    }

    /**
     * Helper method to get the first flag starting with a given prefix.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return The first flag starting with the given prefix, or an empty string if not found.
     */
    public String firstFlag(@NotNull String... prefixes) {
        try {
            return flagsStarting(prefixes)
                    .getFirst();
        } catch (@NotNull NoSuchElementException ignored) {
            return "";
        }
    }

    /**
     * Helper method to get the first flag starting with a given prefix.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return The first flag starting with the given prefix, or an empty string if not found.
     */
    public String firstFlag(@NotNull Collection<String> prefixes) {
        return firstFlag(prefixes.toArray(new String[0]));
    }

    /**
     * Helper method to get the last flag.
     *
     * @return The last flag in the command, or an empty string if not found.
     */
    public String lastFlag() {
        try {
            return flagsStarting("--")
                    .getLast();
        } catch (@NotNull NoSuchElementException e) {
            try {
                return flagsStarting("-")
                        .getLast();
            } catch (@NotNull NoSuchElementException ignored) {
                return "";
            }
        }
    }

    /**
     * Helper method to get the last flag starting with a given prefix.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return The last flag starting with the given prefix, or an empty string if not found.
     */
    public String lastFlag(@NotNull String... prefixes) {
        try {
            return flagsStarting(prefixes)
                    .getLast();
        } catch (@NotNull NoSuchElementException ignored) {
            return "";
        }
    }

    /**
     * Helper method to get the last flag starting with a given prefix.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return The last flag starting with the given prefix, or an empty string if not found.
     */
    public String lastFlag(@NotNull Collection<String> prefixes) {
        return lastFlag(prefixes.toArray(new String[0]));
    }

    /**
     * Helper method to get the number of flags starting with a given prefix.
     *
     * @param prefixes The prefixes of the flags to retrieve.
     * @return The number of flags starting with the given prefix.
     */
    public int flagCount(@NotNull Collection<String> prefixes) {
        return flagCount(prefixes.toArray(new String[0]));
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
     * Retrieves a command argument by its name and converts it to a color.
     *
     * @param arg the name of the argument.
     * @return the argument value converted to a color.
     */
    public TextColor argColor(@NotNull String arg) {
        return context.getArgument(arg, TextColor.class);
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
     * Retrieves a command argument by its name and converts it to a text color.
     * If the argument is not present or invalid, returns the default value.
     *
     * @param arg the name of the argument.
     * @param def the default value to return if the argument is not present.
     * @return the argument value converted to a text color, or the default value if not present or invalid.
     */
    public TextColor argColor(@NotNull String arg, @Nullable TextColor def) {
        try {
            return argColor(arg);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Retrieves the plain sender type of the given context.
     *
     * @return the plain sender type, can be "player", "console", "remote_console", "entity", "command_block", "proxied_sender" or "unknown".
     */
    public String plainSender() {
        return switch (senderType()) {
            case PLAYER -> "player";
            case CONSOLE -> "console";
            case REMOTE_CONSOLE -> "remote_console";
            case ENTITY -> "entity";
            case COMMAND_BLOCK -> "command_block";
            case PROXIED -> "proxied_sender";
            default -> "unknown";
        };
    }

    /**
     * Retrieves the friendly sender type of the given context.
     *
     * @return the friendly sender type, can be "Player", "Console", "Remote Console", "Entity", "Command Block", "Proxied Command Sender" or "Unknown".
     */
    public String friendlySender() {
        return switch (senderType()) {
            case PLAYER -> "Player";
            case CONSOLE -> "Console";
            case REMOTE_CONSOLE -> "Remote Console";
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
     * Checks if the sender is a remote console.
     *
     * @return true if the sender is a remote console, false otherwise.
     */
    public boolean isRemoteConsole() {
        return senderType() == SenderTypes.REMOTE_CONSOLE;
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
        if (sender() instanceof Player) return SenderTypes.PLAYER;
        if (sender() instanceof ConsoleCommandSender) return SenderTypes.CONSOLE;
        if (sender() instanceof RemoteConsoleCommandSender) return SenderTypes.REMOTE_CONSOLE;
        if (sender() instanceof Entity) return SenderTypes.ENTITY;
        if (sender() instanceof BlockCommandSender) return SenderTypes.COMMAND_BLOCK;
        if (sender() instanceof ProxiedCommandSender) return SenderTypes.PROXIED;
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
     * Throws a {@link CmdException} with the given message if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param message   the message to include in the exception
     * @throws CmdException if the predicate is true
     */
    public void throwIf(@NotNull BooleanChecker predicate, @NotNull String message) {
        if (predicate.check()) {
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
     * Throws a {@link CmdException} with the given message if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param message   the message to include in the exception
     * @throws CmdException if the predicate is false
     */
    public void throwIfNot(@NotNull BooleanChecker predicate, @NotNull String message) {
        if (!predicate.check()) {
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
     * Throws a {@link CmdException} with the given component message if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param message   the component message to include in the exception
     * @throws CmdException if the predicate is true
     */
    public void throwIf(@NotNull BooleanChecker predicate, @NotNull Component message) {
        if (predicate.check()) {
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
     * Throws a {@link CmdException} with the given component message if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param message   the component message to include in the exception
     * @throws CmdException if the predicate is false
     */
    public void throwIfNot(@NotNull BooleanChecker predicate, @NotNull Component message) {
        if (!predicate.check()) {
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
     * Throws a {@link CmdException} with the given runnable if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is true
     * @throws CmdException if the predicate is true
     */
    public void throwAndRunIf(@NotNull BooleanChecker predicate, @NotNull Runnable runnable) {
        if (predicate.check()) {
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
     * Throws a {@link CmdException} with the given runnable if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is false
     * @throws CmdException if the predicate is false
     */
    public void throwAndRunIfNot(@NotNull BooleanChecker predicate, @NotNull Runnable runnable) {
        if (!predicate.check()) {
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
     * Runs the given runnable if the given predicate is true.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is true
     */
    public void runIf(@NotNull BooleanChecker predicate, @NotNull Runnable runnable) {
        if (predicate.check()) {
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
     * Runs the given runnable if the given predicate is false.
     *
     * @param predicate the predicate to evaluate
     * @param runnable  the runnable to run if the predicate is false
     */
    public void runIfNot(@NotNull BooleanChecker predicate, @NotNull Runnable runnable) {
        if (!predicate.check()) {
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
        SenderTypes.valueOf(type.name()).check(this);
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
                type == CheckType.REMOTE_CONSOLE && isRemoteConsole() ||
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
        SenderTypes.valueOf(type.name()).check(this, message);
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
     * @param check the custom check to add
     * @throws CmdException if the check fails
     */
    public void check(@NotNull BooleanChecker check) {
        if (!check.check()) {
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
    public void check(@NotNull BooleanChecker check, @NotNull String message) {
        if (!check.check()) {
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
     * If the check fails, a CmdException is thrown.
     *
     * @param check   the custom check to add
     * @param message the custom message to be sent if the check fails
     * @throws CmdException if the check fails
     */
    public void check(@NotNull BooleanChecker check, @NotNull Component message) {
        if (!check.check()) {
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
            check(() -> false);
        }
    }

    /**
     * Adds a custom check to the command.
     * If the check fails, the specified task is executed.
     *
     * @param check the custom check to add
     * @param task  the task to execute if the check fails
     */
    public void check(@NotNull BooleanChecker check, @NotNull Runnable task) {
        if (!check.check()) {
            task.run();
            check(() -> false);
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