package dev.vansen.commandutils.exceptions;

import dev.vansen.commandutils.legacy.LegacyColorsTranslator;
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
     * The type of message to be sent to the command sender, this will only be used if the constructor is called with a message type.
     */
    private @Nullable SendType type;

    /**
     * The messages to be sent to the command sender, this will only be used if the constructor is called with a message type.
     */
    private @Nullable List<String> messages;

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

    /**
     * Constructs a new {@link CmdException} with the specified message type and sender.
     *
     * @param message the message type of the exception.
     * @param sender  the {@link CommandSender} to whom the error message should be sent.
     */
    public CmdException(@NotNull MessageTypes message, @Nullable CommandSender sender) {
        super(message.messages().getFirst());
        this.sender = sender;
        this.type = message.type();
        this.messages = message.messages();
    }

    /**
     * Sends the exception message to the command sender.
     * This method sends the message using the {@link CommandSender#sendRichMessage(String)} or {@link CommandSender#sendMessage(Component)} or {@link CommandSender#sendActionBar(Component)}.
     */
    public void send() {
        if (sender == null) return;
        if (type != null && messages != null) {
            switch (type) {
                case MESSAGE ->
                        messages.forEach(message -> sender.sendRichMessage(LegacyColorsTranslator.translate(message)));
                case ACTION_BAR ->
                        messages.forEach(message -> sender.sendActionBar(MiniMessage.miniMessage().deserializeOrNull(LegacyColorsTranslator.translate(message))));
                case BOTH -> messages.forEach(message -> {
                    sender.sendRichMessage(LegacyColorsTranslator.translate(message));
                    sender.sendActionBar(MiniMessage.miniMessage().deserializeOrNull(LegacyColorsTranslator.translate(message)));
                });
            }
        }
        if (message != null) sender.sendMessage(message);
        else if (getMessage() != null) sender.sendRichMessage(LegacyColorsTranslator.translate(getMessage()));
    }
}