package dev.vansen.commandutils.messages;

/**
 * Represents the different types of messages that can be sent to a sender.
 * The default is MESSAGE.
 */
public enum SendType {

    /**
     * Represents a message that is sent as a message to the sender.
     */
    MESSAGE,

    /**
     * Represents a message that is sent as an action bar to the sender.
     */
    ACTION_BAR,

    /**
     * Represents a message that is sent as both a message and an action bar to the sender.
     */
    BOTH
}