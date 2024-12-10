package dev.vansen.commandutils.messages;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public enum MessageTypes {
    PLAYER_EXCEPTION(List.of("<color:#ff4060>You must be a player to execute this command!</color>"), SendType.MESSAGE),
    CONSOLE_EXCEPTION(List.of("<color:#ff4060>You must execute this command from the console!</color>"), SendType.MESSAGE),
    REMOTE_CONSOLE_EXCEPTION(List.of("<color:#ff4060>You must execute this command as a remote console!</color>"), SendType.MESSAGE),
    ENTITY_EXCEPTION(List.of("<color:#ff4060>You must be an entity to execute this command!</color>"), SendType.MESSAGE),
    COMMAND_BLOCK_EXCEPTION(List.of("<color:#ff4060>This can only be executed by a command block!</color>"), SendType.MESSAGE),
    PROXIED_SENDER_EXCEPTION(List.of("<color:#ff4060>You must be a proxied command sender to execute this command!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_PLAYER(List.of("<color:#ff4060>You are not allowed to execute this as a player!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_CONSOLE(List.of("<color:#ff4060>You are not allowed to execute this from the console!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_REMOTE_CONSOLE(List.of("<color:#ff4060>You are not allowed to execute this as a remote console!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_ENTITY(List.of("<color:#ff4060>You are not allowed to execute this as an entity!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_COMMAND_BLOCK(List.of("<color:#ff4060>You are not allowed to execute this from a command block!</color>"), SendType.MESSAGE),
    NOT_ALLOWED_PROXIED_SENDER(List.of("<color:#ff4060>You are not allowed to execute this as a proxied command sender!</color>"), SendType.MESSAGE);

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
    public void messages(@NotNull List<String> messages) {
        this.messages = messages;
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
    public void type(@NotNull SendType type) {
        this.type = type;
    }
}