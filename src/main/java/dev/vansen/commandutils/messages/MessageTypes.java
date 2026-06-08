package dev.vansen.commandutils.messages;

import dev.vansen.commandutils.exceptions.CommandException;
import dev.vansen.commandutils.exceptions.renderer.ExceptionRenderer;
import dev.vansen.commandutils.exceptions.renderer.ExceptionRenderers;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.util.List;

@SuppressWarnings("unused")
public enum MessageTypes {
    PLAYER_EXCEPTION(List.of("<color:#ff4060>You must be a player to execute this command!</color>"), SendType.MESSAGE),
    CONSOLE_EXCEPTION(List.of("<color:#ff4060>You must execute this command from the console!</color>"), SendType.MESSAGE),
    REMOTE_CONSOLE_EXCEPTION(List.of("<color:#ff4060>You must execute this command as a remote console!</color>"), SendType.MESSAGE),
    ENTITY_EXCEPTION(List.of("<color:#ff4060>You must be an entity to execute this command!</color>"), SendType.MESSAGE),
    COMMAND_BLOCK_EXCEPTION(List.of("<color:#ff4060>This can only be executed by a command block!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_PLAYER(List.of("<color:#ff4060>You are not allowed to execute this as a player!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_CONSOLE(List.of("<color:#ff4060>You are not allowed to execute this from the console!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_REMOTE_CONSOLE(List.of("<color:#ff4060>You are not allowed to execute this as a remote console!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_ENTITY(List.of("<color:#ff4060>You are not allowed to execute this as an entity!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_COMMAND_BLOCK(List.of("<color:#ff4060>You are not allowed to execute this from a command block!</color>"), SendType.MESSAGE),
    GENERIC_EXCEPTION(List.of("<hover_color=#ff4060><color:#ff4060>An unexpected error occured while executing the command, hover over this message for more information.</color>"), SendType.MESSAGE);

    private @NotNull List<String> messages;
    private @NotNull SendType type;

    MessageTypes(@NotNull List<String> messages, @NotNull SendType type) {
        this.messages = messages;
        this.type = type;
    }

    /**
     * The messages that will be sent to the command sender.
     *
     * @return the messages that will be sent
     */
    public @NotNull List<String> messages() {
        return messages;
    }

    /**
     * Sets the messages that will be sent to the command sender.
     *
     * @param messages the messages to set
     */
    public MessageTypes messages(@NotNull List<String> messages) {
        this.messages = messages;
        return this;
    }

    /**
     * The type of message that will be sent to the command sender.
     *
     * @return the type of message that will be sent
     */
    public @NotNull SendType type() {
        return type;
    }

    /**
     * Sets the type of message that will be sent to the command sender.
     *
     * @param type the type of message to set
     */
    public MessageTypes type(@NotNull SendType type) {
        this.type = type;
        return this;
    }

    /**
     * Sends an unexpected command error to a sender.
     *
     * @param sender the command sender
     * @param e the throwable
     */
    public static void sendUnexpectedError(@Nullable CommandSender sender, @NotNull Throwable e) {
        LoggerFactory.getLogger("CommandUtils")
                .error("An unexpected error occurred while executing command", e);

        if (sender == null) return;

        if (e instanceof CommandException ce) {
            ce.send(sender);
            return;
        }

        ExceptionRenderer renderer = ExceptionRenderers.find(e);
        if (renderer != null) {
            renderer.render(sender, e);
            return;
        }

        sendGeneric(sender, e);
    }

    /**
     * Sends a generic error message to the sender, with hover text containing the exception message.
     *
     * @param sender the command sender
     * @param e the throwable
     */
    public static void sendGeneric(@NotNull CommandSender sender, @NotNull Throwable e) {
        MessageTypes.GENERIC_EXCEPTION.messages().forEach(msg -> {
            int i = msg.indexOf("<hover_color=#");
            if (i == -1) {
                sender.sendRichMessage(msg);
                return;
            }

            int j = msg.indexOf(">", i);
            String hover = e.getMessage() != null
                    ? e.getMessage()
                    : "No additional information.";

            sender.sendRichMessage(
                    "<hover:show_text:'<color:#" + msg.substring(i + 14, j) + ">"
                            + hover + "</color>'>"
                            + msg.substring(0, i)
                            + msg.substring(j + 1)
                            + "</hover>"
            );
        });
    }
}