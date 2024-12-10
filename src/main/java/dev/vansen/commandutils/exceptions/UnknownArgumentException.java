package dev.vansen.commandutils.exceptions;

import dev.vansen.commandutils.argument.finder.ArgumentString;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception thrown when an unknown argument is provided to a command (View {@link ArgumentString#types} for a list of valid arguments)
 */
public class UnknownArgumentException extends RuntimeException {

    /**
     * Creates a new UnknownArgumentException.
     */
    public UnknownArgumentException(@NotNull String argument, @NotNull List<String> validArguments) {
        super(String.format("Unknown argument '%s'. Did you mean one of: %s?", argument, validArguments.stream().map(arg -> "* " + arg).collect(Collectors.joining("\n"))));
    }
}
