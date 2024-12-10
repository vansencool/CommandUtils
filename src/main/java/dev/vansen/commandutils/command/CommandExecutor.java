package dev.vansen.commandutils.command;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a command executor responsible for handling the execution logic of commands.
 */
@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executes the command with the given context.
     *
     * @param context the {@link CommandWrapper} containing context information for the command.
     */
    void execute(@NotNull CommandWrapper context);
}