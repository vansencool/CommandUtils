package dev.vansen.commandutils.completer.info;

import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * A utility class for accessing argument details from a SuggestionsBuilderWrapper.
 */
@SuppressWarnings("unused")
public final class SuggestionsHelper {
    private final @NotNull String input;
    private final @NotNull String inputWithoutCommand;
    private final @NotNull List<String> arguments;

    public SuggestionsHelper(@NotNull SuggestionsBuilderWrapper wrapper) {
        this.input = wrapper.input().trim();
        this.inputWithoutCommand = String.join(" ", Arrays.stream(input.split(" "))
                .skip(1)
                .toArray(String[]::new));
        this.arguments = List.of(Arrays.stream(input.split(" "))
                .skip(1)
                .toArray(String[]::new));
    }

    /**
     * Gets the arguments before the cursor.
     *
     * @return all arguments before the cursor as a string
     */
    public @NotNull String previousArgs() {
        String[] args = input.split(" ");
        if (args.length < 2) return "";
        return String.join(" ", Arrays.copyOfRange(args, 1, args.length - 1));
    }

    /**
     * Gets the last argument before the cursor.
     *
     * @return the last argument before the cursor
     */
    public @NotNull String previousArg() {
        if (inputWithoutCommand.lastIndexOf(" ") == -1) return "";
        return inputWithoutCommand.substring(inputWithoutCommand.lastIndexOf(" ", inputWithoutCommand.lastIndexOf(" ") - 1) + 1, inputWithoutCommand.lastIndexOf(" "));
    }

    /**
     * Gets the current argument being typed.
     *
     * @return the current argument
     */
    public @NotNull String currentArg() {
        if (inputWithoutCommand.lastIndexOf(" ") == -1) return inputWithoutCommand;
        return inputWithoutCommand.substring(inputWithoutCommand.lastIndexOf(" ") + 1);
    }

    /**
     * Gets the total number of arguments provided.
     *
     * @return the count of arguments
     */
    public int argCount() {
        return arguments.size();
    }

    /**
     * Gets the argument at the specified index.
     *
     * @param index the index
     * @return the argument at the index, or an empty string if out of bounds
     */
    public @NotNull String argAt(int index) {
        return index >= 0 && index < arguments.size() ? arguments.get(index) : "";
    }

    /**
     * Gets the arguments before the specified index.
     *
     * @param index the index
     * @return the arguments before the index as a string
     */
    public @NotNull String argsBefore(int index) {
        if (index < 0) return "";
        if (index >= arguments.size()) return String.join(" ", arguments.subList(0, arguments.size()));
        return String.join(" ", arguments.subList(0, index));
    }

    /**
     * Gets the arguments between the specified indices.
     *
     * @param start the start index
     * @param end   the end index
     * @return the arguments between the indices as a string
     */
    public @NotNull String argsBetween(int start, int end) {
        if (start < 0 || end < 0) return "";
        if (start >= arguments.size() || end >= arguments.size()) return "";
        return String.join(" ", arguments.subList(start, end));
    }

    /**
     * Checks if the specified index has an argument.
     *
     * @param index the index
     * @return true if the index has an argument, false otherwise
     */
    public boolean hasArg(int index) {
        return index >= 0 && index < arguments.size();
    }

    /**
     * Checks if the argument at the specified index is empty.
     *
     * @param index the index
     * @return true if the argument is empty, false otherwise
     */
    public boolean isEmptyAt(int index) {
        return argAt(index).isEmpty();
    }

    /**
     * Gets the arguments after the specified index.
     *
     * @param index the index
     * @return the arguments after the index as a string
     */
    public @NotNull String argsAfter(int index) {
        if (index < 0) return "";
        if (index >= arguments.size()) return "";
        return String.join(" ", arguments.subList(index, arguments.size()));
    }
}