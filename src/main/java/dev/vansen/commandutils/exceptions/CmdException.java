package dev.vansen.commandutils.exceptions;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an exception that occurs during command execution in a Minecraft plugin.
 * This class extends {@link CommandException} and is used to send a custom error message
 * to a {@link CommandSender}.
 */
public class CmdException extends CommandException {

    /**
     * The sender of the command to which the exception message will be sent.
     */
    private final @NotNull CommandSender sender;

    /**
     * Constructs a new {@link CmdException} with the specified message and sender.
     *
     * @param message the detail message of the exception.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@NotNull String message, @NotNull CommandSender sender) {
        super(message);
        this.sender = sender;
    }

    /**
     * Sends the exception message to the command sender.
     * This method sends the message using the {@code sendRichMessage} method of {@link CommandSender}.
     */
    public void send() {
        sender.sendRichMessage(getMessage());
    }
}