package dev.vansen.commandutils.exceptions;

import dev.vansen.commandutils.messages.MessageTypes;
import dev.vansen.commandutils.messages.SendType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents an exception that occurs during command execution.
 * This class extends {@link CommandException} and is used to send a custom error message
 * to a {@link CommandSender}.
 */
@SuppressWarnings("unused")
public class CmdException extends RuntimeException {

    /**
     * The sender of the command to which the exception message will be sent.
     */
    private final @Nullable CommandSender sender;

    /**
     * The message to be sent to the command sender.
     */
    private @Nullable Component message;

    /**
     * The type of message to be sent to the command sender, this will only be used if the constructor is called with a message type.
     */
    private @Nullable SendType type;

    /**
     * The messages to be sent to the command sender, this will only be used if the constructor is called with a message type.
     */
    private @Nullable List<String> messages;

    /**
     * Constructs a new {@link CmdException} with a plain string message and no sender.
     *
     * @param message the detail message of the exception.
     */
    public CmdException(@Nullable String message) {
        super(message);
        this.message = MiniMessage.miniMessage().deserializeOrNull(message);
        this.sender = null;
    }

    /**
     * Constructs a new {@link CmdException} with a component message and no sender.
     *
     * @param message the component message of the exception.
     */
    public CmdException(@Nullable Component message) {
        super(PlainTextComponentSerializer.plainText().serializeOrNull(message));
        this.message = message;
        this.sender = null;
    }

    /**
     * Constructs a new {@link CmdException} with a plain string message and sender.
     *
     * @param message the detail message of the exception.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@Nullable String message, @Nullable CommandSender sender) {
        super(message);
        this.message = MiniMessage.miniMessage().deserializeOrNull(message);
        this.sender = sender;
    }

    /**
     * Constructs a new {@link CmdException} with a component message and sender.
     *
     * @param message the component message of the exception.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@Nullable Component message, @Nullable CommandSender sender) {
        super(PlainTextComponentSerializer.plainText().serializeOrNull(message));
        this.message = message;
        this.sender = sender;
    }

    /**
     * Constructs a new {@link CmdException} from a predefined {@link MessageTypes} with no sender.
     *
     * @param message the message type containing send type and messages.
     */
    public CmdException(@NotNull MessageTypes message) {
        super(message.messages().getFirst());
        this.sender = null;
        this.type = message.type();
        this.messages = message.messages();
    }

    /**
     * Constructs a new {@link CmdException} from a predefined {@link MessageTypes} and sender.
     *
     * @param message the message type containing send type and messages.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@NotNull MessageTypes message, @Nullable CommandSender sender) {
        super(message.messages().getFirst());
        this.sender = sender;
        this.type = message.type();
        this.messages = message.messages();
    }

    /**
     * Constructs a new {@link CmdException} with multiple messages and a send type, without a sender.
     *
     * @param messages the messages to be sent.
     * @param type     the {@link SendType} defining how the messages are sent.
     */
    public CmdException(@NotNull List<String> messages, @NotNull SendType type) {
        super(messages.getFirst());
        this.sender = null;
        this.type = type;
        this.messages = messages;
    }

    /**
     * Constructs a new {@link CmdException} with multiple messages, a send type, and a sender.
     *
     * @param messages the messages to be sent.
     * @param type     the {@link SendType} defining how the messages are sent.
     * @param sender   the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@NotNull List<String> messages, @NotNull SendType type, @Nullable CommandSender sender) {
        super(messages.getFirst());
        this.sender = sender;
        this.type = type;
        this.messages = messages;
    }

    /**
     * Constructs a new {@link CmdException} with a single message and send type, without a sender.
     *
     * @param message the message to be sent.
     * @param type    the {@link SendType} defining how the message is sent.
     */
    public CmdException(@NotNull String message, @NotNull SendType type) {
        super(message);
        this.sender = null;
        this.type = type;
        this.messages = List.of(message);
    }

    /**
     * Constructs a new {@link CmdException} with a single message, send type, and sender.
     *
     * @param message the message to be sent.
     * @param type    the {@link SendType} defining how the message is sent.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@NotNull String message, @NotNull SendType type, @Nullable CommandSender sender) {
        super(message);
        this.sender = sender;
        this.type = type;
        this.messages = List.of(message);
    }

    /**
     * Constructs a new {@link CmdException} with multiple messages using varargs and a send type, without a sender.
     *
     * @param type     the {@link SendType} defining how the messages are sent.
     * @param messages the messages to be sent.
     */
    public CmdException(@NotNull SendType type, @NotNull String... messages) {
        super(messages.length > 0 ? messages[0] : null);
        this.sender = null;
        this.type = type;
        this.messages = List.of(messages);
    }

    /**
     * Constructs a new {@link CmdException} with multiple messages using varargs, a send type, and a sender.
     *
     * @param type     the {@link SendType} defining how the messages are sent.
     * @param sender   the {@link CommandSender} to whom the error message should be sent.
     * @param messages the messages to be sent.
     */
    public CmdException(@NotNull SendType type, @Nullable CommandSender sender, @NotNull String... messages) {
        super(messages.length > 0 ? messages[0] : null);
        this.sender = sender;
        this.type = type;
        this.messages = List.of(messages);
    }

    /**
     * Sends the exception message to the command sender.
     * This method sends the message using the {@link CommandSender#sendRichMessage(String)},
     * {@link CommandSender#sendMessage(Component)}, or {@link CommandSender#sendActionBar(Component)}
     * depending on the configured {@link SendType}.
     */
    public void send() {
        if (sender == null) return;

        if (type != null && messages != null) {
            switch (type) {
                case MESSAGE -> messages.forEach(sender::sendRichMessage);
                case ACTION_BAR -> messages.forEach(m -> sender.sendActionBar(MiniMessage.miniMessage().deserializeOrNull(m)));
                case BOTH -> messages.forEach(m -> {
                    sender.sendRichMessage(m);
                    sender.sendActionBar(MiniMessage.miniMessage().deserializeOrNull(m));
                });
            }
            return;
        }

        if (message != null) sender.sendMessage(message);
    }
}