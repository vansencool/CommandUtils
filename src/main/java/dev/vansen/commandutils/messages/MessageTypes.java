package dev.vansen.commandutils.messages;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public enum MessageTypes {
    PLAYER_EXCEPTION("<color:#ff4060>Exception: You must be a player to execute this command!</color>"),
    CONSOLE_EXCEPTION("<color:#ff4060>Exception: You must execute this command from the console!</color>"),
    ENTITY_EXCEPTION("<color:#ff4060>Exception: You must be an entity to execute this command!</color>"),
    COMMAND_BLOCK_EXCEPTION("<color:#ff4060>Exception: This can only be executed by a command block!</color>"),
    PROXIED_SENDER_EXCEPTION("<color:#ff4060>Exception: You must be a proxied command sender to execute this command!</color>"),
    NOT_ALLOWED_PLAYER("<color:#ff4060>You are not allowed to execute this as a player!</color>"),
    NOT_ALLOWED_CONSOLE("<color:#ff4060>You are not allowed to execute this from the console!</color>"),
    NOT_ALLOWED_ENTITY("<color:#ff4060>You are not allowed to execute this as an entity!</color>"),
    NOT_ALLOWED_COMMAND_BLOCK("<color:#ff4060>You are not allowed to execute this from a command block!</color>"),
    NOT_ALLOWED_PROXIED_SENDER("<color:#ff4060>You are not allowed to execute this as a proxied command sender!</color>");

    private String message;

    MessageTypes(@NotNull String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public void message(@NotNull String message) {
        this.message = message;
    }
}
