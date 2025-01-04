package dev.vansen.commandutils.bukkit.argument;

import dev.vansen.commandutils.argument.arguments.color.ArgumentColors;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.optional.qual.MaybePresent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for accessing arguments in a command.
 */
@SuppressWarnings("unused")
public class BukkitArgument {
    private final @NotNull CommandWrapper context;
    private final @NotNull ArgumentParser parser;

    /**
     * Constructs a new BukkitArgument.
     *
     * @param context The command context.
     * @param args    The arguments.
     */
    public BukkitArgument(@NotNull CommandWrapper context, @NotNull String args) {
        this.context = context;
        this.parser = new ArgumentParser(args);
    }

    /**
     * Retrieves the argument of the specified name.
     *
     * @param arg The index of the argument to retrieve.
     * @return The argument at the specified index, or an empty string if the index is out of bounds or the argument is empty.
     */
    public @NotNull String arg(@NotNull String arg) {
        return context.arg(parser.index(arg));
    }

    /**
     * Retrieves the argument at the specified index.
     * This is 1 index based, so index 0 is the command itself, index 1 is the first argument
     *
     * @param index The index of the argument to retrieve.
     * @return The argument at the specified index.
     */
    public @NotNull String argOf(int index) {
        return context.arg(index);
    }

    /**
     * Retrieves the argument at the specified index, or a default value if it is empty.
     * This is 1 index based, so index 0 is the command itself, index 1 is the first argument
     *
     * @param index The index of the argument to retrieve.
     * @param def   The default value to return if the argument is empty.
     * @return The argument at the specified index, or the default value if it is empty.
     */
    public @NotNull String argOfOr(int index, @NotNull String def) {
        return context.arg(index).isEmpty() ? def : context.arg(index);
    }

    /**
     * Retrieves the argument at the specified index, or a default value if it is empty.
     * This is 1 index based, so index 0 is the command itself, index 1 is the first argument
     *
     * @param index The index of the argument to retrieve.
     * @param def   The default value to return if the argument is empty.
     * @return The argument at the specified index, or the default value if it is empty.
     */
    public @NotNull String argOfOrDefault(int index, @NotNull String def) {
        return argOfOr(index, def);
    }

    /**
     * Retrieves the argument at the specified index.
     * This is 0 index based, so index 0 is the first argument, index 1 is the second argument, ect
     *
     * @param index The index of the argument to retrieve.
     * @return The argument at the specified index.
     */
    public @NotNull String argOfIndex(int index) {
        return context().inputWithoutCommand().split("\\s+")[index];
    }

    /**
     * Retrieves the argument at the specified index, or a default value if it is empty.
     * This is 0 index based, so index 0 is the first argument, index 1 is the second argument, ect
     *
     * @param index The index of the argument to retrieve.
     * @param def   The default value to return if the argument is empty.
     * @return The argument at the specified index, or the default value if it is empty.
     */
    public @NotNull String argOfIndexOr(int index, @NotNull String def) {
        return context().inputWithoutCommand().split("\\s+")[index].isEmpty() ? def : context().inputWithoutCommand().split("\\s+")[index];
    }

    /**
     * Retrieves the argument at the specified index, or a default value if it is empty.
     * This is 0 index based, so index 0 is the first argument, index 1 is the second argument, ect
     *
     * @param index The index of the argument to retrieve.
     * @param def   The default value to return if the argument is empty.
     * @return The argument at the specified index, or the default value if it is empty.
     */
    public @NotNull String argOfIndexOrDefault(int index, @NotNull String def) {
        return argOfIndexOr(index, def);
    }

    /**
     * Retrieves the argument of the specified name, or a default value if it is empty.
     *
     * @param arg The index of the argument to retrieve.
     * @param def The default value to return if the argument is empty.
     * @return The argument of the specified name, or the default value if it is empty.
     */
    public @NotNull String argOr(@NotNull String arg, @NotNull String def) {
        return context.arg(parser.index(arg)).isEmpty() ? def : context.arg(parser.index(arg));
    }

    /**
     * Retrieves the argument of the specified name, or a default value if it is empty.
     *
     * @param arg The index of the argument to retrieve.
     * @param def The default value to return if the argument is empty.
     * @return The argument of the specified name, or the default value if it is empty.
     */
    public @NotNull String argOrDefault(@NotNull String arg, @NotNull String def) {
        return argOr(arg, def);
    }

    /**
     * Checks if the specified argument is a valid color.
     *
     * @param argName The name of the argument to check.
     * @return True if the argument is a color, false otherwise.
     */
    public boolean parseColor(@NotNull String argName) {
        if (ArgumentColors.COLOR_MAP.containsKey(arg(argName).toLowerCase())) {
            return TextColor.fromHexString(ArgumentColors.COLOR_MAP.getOrDefault(arg(argName), "")) != null;
        } else {
            return TextColor.fromHexString(arg(argName)) != null;
        }
    }

    /**
     * Checks if the specified argument is a valid player.
     *
     * @param argName The name of the argument to check.
     * @return True if the argument is a player, false otherwise.
     */
    public boolean parsePlayer(@NotNull String argName) {
        return Bukkit.getPlayer(arg(argName)) != null;
    }

    /**
     * Checks if the specified argument is a valid color.
     * Else throws an exception.
     *
     * @param argName The name of the argument to check.
     */
    public void parseColorOrThrow(@NotNull String argName) {
        if (!parseColor(argName)) {
            throw new CmdException("Invalid color: " + arg(argName), context.sender());
        }
    }

    /**
     * Checks if the specified argument is a valid player.
     * Else throws an exception.
     *
     * @param argName The name of the argument to check.
     */
    public void parsePlayerOrThrow(@NotNull String argName) {
        if (!parsePlayer(argName)) {
            throw new CmdException("Invalid player: " + arg(argName), context.sender());
        }
    }

    /**
     * Retrieves the color of the specified argument.
     * It is recommended to not use this before doing {@link #parseColor(String)} to avoid errors.
     *
     * @param argName The name of the argument to retrieve.
     * @return The color of the argument.
     */
    @MaybePresent
    @NotNull
    public Optional<TextColor> color(@NotNull String argName) {
        if (ArgumentColors.COLOR_MAP.containsKey(arg(argName).toLowerCase())) {
            return Optional.ofNullable(TextColor.fromHexString(ArgumentColors.COLOR_MAP.get(arg(argName))));
        } else {
            return Optional.ofNullable(TextColor.fromHexString(arg(argName)));
        }
    }

    /**
     * Retrieves the player of the specified argument.
     * It is recommended to not use this before doing {@link #parsePlayer(String)} to avoid errors.
     *
     * @param argName The name of the argument to retrieve.
     * @return The player of the argument.
     */
    @MaybePresent
    @NotNull
    public Optional<Player> player(@NotNull String argName) {
        return Optional.ofNullable(Bukkit.getPlayer(arg(argName)));
    }

    /**
     * Retrieves the command context.
     *
     * @return The command context.
     */
    public @NotNull CommandWrapper context() {
        return context;
    }

    /**
     * Returns a copied collection of the command arguments, allowing for safe manipulation and querying of the arguments.
     *
     * @return a copied collection of the command arguments
     */
    public @NotNull ArrayList<String> copied() {
        return new ArrayList<>(List.of(context.inputWithoutCommand().split("\\s+")));
    }

    /**
     * Retrieves the argument parser used to parse the arguments.
     *
     * @return The argument parser.
     */
    public @NotNull ArgumentParser parser() {
        return parser;
    }
}