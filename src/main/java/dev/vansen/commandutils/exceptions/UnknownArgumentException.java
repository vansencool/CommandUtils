package dev.vansen.commandutils.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Exception thrown when an unknown argument is provided to a command.
 */
public class UnknownArgumentException extends RuntimeException {

    /**
     * Creates a new UnknownArgumentException.
     */
    public UnknownArgumentException(@NotNull String argument, @NotNull Collection<String> validArguments) {
        super(String.format("Unknown argument '%s'. Did you mean one of: %s?", argument, validArguments.stream().map(arg -> "* " + arg).collect(Collectors.joining("\n"))));
    }
}
