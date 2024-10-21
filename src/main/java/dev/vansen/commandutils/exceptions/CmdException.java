package dev.vansen.commandutils.exceptions;

import dev.vansen.commandutils.messages.MessageTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an exception that occurs during command execution.
 * This class extends {@link CommandException} and is used to send a custom error message
 * to a {@link CommandSender}.
 */
public final class CmdException extends CommandException {

    /**
     * The sender of the command to which the exception message will be sent.
     */
    private final @Nullable CommandSender sender;
    /**
     * The message to be sent to the command sender.
     */
    private @Nullable Component message;

    /**
     * Constructs a new {@link CmdException} with the specified message and sender.
     *
     * @param message the detail message of the exception.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@Nullable String message, @Nullable CommandSender sender) {
        super(message);
        this.sender = sender;
    }

    /**
     * Constructs a new {@link CmdException} with the specified message and sender.
     *
     * @param message the detail message of the exception.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@Nullable Component message, @Nullable CommandSender sender) {
        super(PlainTextComponentSerializer.plainText().serializeOrNull(message));
        this.message = message;
        this.sender = sender;
    }

    public CmdException(@NotNull MessageTypes message, @Nullable CommandSender sender) {
        super(message.message());
        this.sender = sender;
    }

    /**
     * Sends the exception message to the command sender.
     * This method sends the message using the {@code sendRichMessage} or {@code sendMessage} method of {@link CommandSender}.
     */
    public void send() {
        if (sender == null || getMessage() == null && message == null) return;
        if (message != null) sender.sendMessage(message);
        else sender.sendRichMessage(getMessage());
    }
}