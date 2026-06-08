package dev.vansen.commandutils.exceptions;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Base exception for command-related errors that support custom rendering.
 */
public abstract class CommandException extends RuntimeException {

    protected CommandException(@NotNull String message) {
        super(message);
    }

    /**
     * Sends this exception to the given sender.
     *
     * @param sender the command sender
     */
    public abstract void send(@NotNull CommandSender sender);
}
