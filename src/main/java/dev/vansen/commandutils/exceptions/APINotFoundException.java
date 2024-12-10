package dev.vansen.commandutils.exceptions;

/**
 * An exception that is thrown when the command API is not set.
 */
public class APINotFoundException extends RuntimeException {

    /**
     * Creates a new APINotFoundException exception.
     */
    public APINotFoundException() {
        super("Command API not set! Use CommandAPI.set(plugin) or CommandAPI.set(lifecycle event manager) to set the Command API.");
    }
}
