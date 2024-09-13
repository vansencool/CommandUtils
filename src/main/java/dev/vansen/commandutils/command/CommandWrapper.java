package dev.vansen.commandutils.command;

import com.mojang.brigadier.context.CommandContext;
import dev.vansen.commandutils.exceptions.CmdException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
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
            throw new CmdException("<color:#ff4060>Exception -> You must be a player to execute this command</color>", sender());
        }
        if (type == CheckType.CONSOLE && sender() instanceof Player) {
            throw new CmdException("<color:#ff4060>Exception -> You must execute this command from the console</color>", sender());
        }
        if (type == CheckType.ENTITY && !(sender() instanceof Entity)) {
            throw new CmdException("<color:#ff4060>Exception -> You must be an entity to execute this command</color>", sender());
        }
    }

    /**
     * Checks whether the command sender meets the specified condition and throws a custom exception if not.
     *
     * @param type         the type of check to be performed.
     * @param errorMessage the custom error message to be sent if the check fails.
     * @throws CmdException if the check fails.
     */
    public void check(@NotNull CheckType type, @NotNull String errorMessage) {
        if (type == CheckType.PLAYER && !(sender() instanceof Player)) {
            throw new CmdException(errorMessage, sender());
        }
        if (type == CheckType.CONSOLE && sender() instanceof Player) {
            throw new CmdException(errorMessage, sender());
        }
        if (type == CheckType.ENTITY && !(sender() instanceof Entity)) {
            throw new CmdException(errorMessage, sender());
        }
    }
}