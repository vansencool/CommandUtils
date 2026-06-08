package dev.vansen.commandutils.info;

import java.util.List;

/**
 * Represents a collection of aliases for a command.
 *
 * @param aliases The list of aliases for the command.
 */
@SuppressWarnings("unused")
public record Aliases(List<String> aliases) {

    /**
     * Creates a new Aliases instance from an array of strings.
     *
     * @param aliases The aliases for the command.
     */
    public Aliases(String... aliases) {
        this(List.of(aliases));
    }

    /**
     * Returns a new Aliases instance from an array of strings.
     *
     * @param aliases The aliases for the command.
     * @return A new Aliases instance.
     */
    public static Aliases of(String... aliases) {
        return new Aliases(aliases);
    }

    /**
     * Returns a new Aliases instance from a list of strings.
     *
     * @param aliases The aliases for the command.
     * @return A new Aliases instance.
     */
    public static Aliases of(List<String> aliases) {
        return new Aliases(aliases);
    }

    /**
     * Returns the list of aliases for the command.
     *
     * @return The list of aliases.
     */
    @Override
    public List<String> aliases() {
        return aliases;
    }
}