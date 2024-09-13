package dev.vansen.commandutils.command;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a command executor responsible for handling the execution logic of commands.
 * <p>
 * Implementations of this interface define how a command should be executed when invoked.
 * The {@link CommandWrapper} provided to the {@link #execute(CommandWrapper)} method
 * contains the context of the command, including arguments and the command sender.
 * </p>
 */
public interface CommandExecutor {

    /**
     * Executes the command with the given context.
     *
     * @param context the {@link CommandWrapper} containing context information for the command.
     * @throws RuntimeException if the execution fails due to some runtime issue.
     */
    void execute(@NotNull CommandWrapper context);
}