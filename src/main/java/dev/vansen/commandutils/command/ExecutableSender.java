package dev.vansen.commandutils.command;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.vansen.commandutils.sender.SenderTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents sender(s) that can execute the default executor.
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
    public static ExecutableSender types(@NotNull SenderTypes... types) {
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
    public static ExecutableSender types(@NotNull List<SenderTypes> types) {
        return new ExecutableSender(types.toArray(new SenderTypes[0]));
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
}