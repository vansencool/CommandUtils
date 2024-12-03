package dev.vansen.commandutils.messages;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum MessageTypes {
    PLAYER_EXCEPTION("<color:#ff4060>You must be a player to execute this command!</color>", SendType.MESSAGE),
    CONSOLE_EXCEPTION("<color:#ff4060>You must execute this command from the console!</color>", SendType.MESSAGE),
    REMOTE_CONSOLE_EXCEPTION("<color:#ff4060>You must execute this command as a remote console!</color>", SendType.MESSAGE),
    ENTITY_EXCEPTION("<color:#ff4060>You must be an entity to execute this command!</color>", SendType.MESSAGE),
    COMMAND_BLOCK_EXCEPTION("<color:#ff4060>This can only be executed by a command block!</color>", SendType.MESSAGE),
    PROXIED_SENDER_EXCEPTION("<color:#ff4060>You must be a proxied command sender to execute this command!</color>", SendType.MESSAGE),
    NOT_ALLOWED_PLAYER("<color:#ff4060>You are not allowed to execute this as a player!</color>", SendType.MESSAGE),
    NOT_ALLOWED_CONSOLE("<color:#ff4060>You are not allowed to execute this from the console!</color>", SendType.MESSAGE),
    NOT_ALLOWED_REMOTE_CONSOLE("<color:#ff4060>You are not allowed to execute this as a remote console!</color>", SendType.MESSAGE),
    NOT_ALLOWED_ENTITY("<color:#ff4060>You are not allowed to execute this as an entity!</color>", SendType.MESSAGE),
    NOT_ALLOWED_COMMAND_BLOCK("<color:#ff4060>You are not allowed to execute this from a command block!</color>", SendType.MESSAGE),
    NOT_ALLOWED_PROXIED_SENDER("<color:#ff4060>You are not allowed to execute this as a proxied command sender!</color>", SendType.MESSAGE);

    private @NotNull String message;
    private @NotNull SendType type;

    MessageTypes(@NotNull String message, @NotNull SendType type) {
        this.message = message;
        this.type = type;
    }

    public @NotNull String message() {
        return message;
    }

    public void message(@NotNull String message) {
        this.message = message;
    }

    public @NotNull SendType type() {
        return type;
    }

    public void type(@NotNull SendType type) {
        this.type = type;
    }
}
