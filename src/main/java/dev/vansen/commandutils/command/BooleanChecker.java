package dev.vansen.commandutils.command;

/**
 * An interface representing a boolean check.
 * <p>
 * Implementations of this interface should provide a single method, {@link #check()},
 * which returns a boolean value indicating the result of the check.
 */
@FunctionalInterface
public interface BooleanChecker {
    /**
     * Performs the boolean check and returns the result.
     *
     * @return the result of the check
     */
    boolean check();
}