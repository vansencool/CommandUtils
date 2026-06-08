package dev.vansen.commandutils.command;

import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.messages.MessageTypes;
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

    /**
     * Executes the command safely, catching any unexpected errors and sending an error message to the sender.
     *
     * @param context the {@link CommandWrapper} containing context information for the command.
     */
    default void executeSafely(@NotNull CommandWrapper context) {
        try {
            execute(context);
        } catch (Throwable e) {
            if (e instanceof CmdException) {
                throw e;
            }
            MessageTypes.sendUnexpectedError(context.sender(), e);
        }
    }
}