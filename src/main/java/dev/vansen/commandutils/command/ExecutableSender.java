package dev.vansen.commandutils.command;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.vansen.commandutils.sender.SenderTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a sender that can execute a command.
 */
@SuppressWarnings("unused")
public final class ExecutableSender {

    private static SenderTypes[] senderTypes;

    /**
     * Creates a new instance of the ExecutableSender class.
     */
    public ExecutableSender(@NotNull SenderTypes[] senderTypes) {
        ExecutableSender.senderTypes = senderTypes;
    }

    /**
     * Sets the types of senders that are allowed to execute the command.
     *
     * @param types the types of senders that are allowed to execute the command.
     * @return this ExecutableSender instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public static ExecutableSender type(@NotNull SenderTypes... types) {
        return new ExecutableSender(types);
    }

    /**
     * Sets the types of senders that are allowed to execute the command from a list.
     *
     * @param types the list of types of senders that are allowed to execute the command.
     * @return this ExecutableSender instance.
     */
    @NotNull
    @CanIgnoreReturnValue
    public static ExecutableSender type(@NotNull List<SenderTypes> types) {
        return new ExecutableSender(types.toArray(new SenderTypes[0]));
    }

    /**
     * Returns a new instance of the ExecutableSender.Builder class.
     *
     * @return a new instance of the ExecutableSender.Builder class.
     */
    @NotNull
    @CanIgnoreReturnValue
    public Builder builder() {
        return new Builder();
    }

    /**
     * Returns the types of senders that are allowed to execute the command.
     *
     * @return the types of senders that are allowed to execute the command.
     */
    @NotNull
    public SenderTypes[] types() {
        return senderTypes;
    }

    /**
     * A builder class for creating new instances of ExecutableSender.
     */
    public static class Builder {
        private SenderTypes[] senderTypes;

        /**
         * Sets the types of senders that are allowed to execute the command.
         *
         * @param types the types of senders that are allowed to execute the command.
         * @return this Builder instance.
         */
        @NotNull
        @CanIgnoreReturnValue
        public Builder type(@NotNull SenderTypes... types) {
            senderTypes = types;
            return this;
        }

        /**
         * Sets the types of senders that are allowed to execute the command from a list.
         *
         * @param types the list of types of senders that are allowed to execute the command.
         * @return this Builder instance.
         */
        @NotNull
        @CanIgnoreReturnValue
        public Builder type(@NotNull List<SenderTypes> types) {
            senderTypes = types.toArray(new SenderTypes[0]);
            return this;
        }

        /**
         * Builds a new instance of ExecutableSender with the specified sender types.
         *
         * @return a new instance of ExecutableSender.
         */
        @NotNull
        @CanIgnoreReturnValue
        public ExecutableSender build() {
            return new ExecutableSender(senderTypes);
        }
    }
}