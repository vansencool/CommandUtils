package dev.vansen.commandutils.bukkit.argument;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A utility class for parsing and accessing command arguments.
 */
@SuppressWarnings("unused")
public class ArgumentParser {
    private final ConcurrentMap<String, Integer> argMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> argRequiredMap = new ConcurrentHashMap<>();
    private final String args;

    /**
     * Constructs a new ArgumentParser object.
     *
     * @param args The arguments to be parsed.
     */
    public ArgumentParser(@NotNull String args) {
        this.args = args;
        parse();
    }

    /**
     * Parses the arguments and populates the internal maps, only public for external libraries expanding upon.
     */
    public void parse() {
        String[] argArray = args.split("\\s+");
        for (int i = 0; i < argArray.length; i++) {
            String arg = argArray[i];
            if (!arg.startsWith("[") && !arg.startsWith("<") && !arg.endsWith("]") && !arg.endsWith(">")) continue;
            argMap.put(arg.replaceAll("[<>\\[\\]]", ""), i + 1);
            if (arg.startsWith("<") && arg.endsWith(">")) argRequiredMap.put(arg.replaceAll("[<>\\[\\]]", ""), true);
        }
    }

    /**
     * Retrieves the index of the specified argument.
     *
     * @param argName The name of the argument to retrieve.
     * @return The index of the argument, or -1 if not found.
     */
    public int index(@NotNull String argName) {
        return argMap.getOrDefault(argName, -1);
    }

    /**
     * Checks if the specified argument is required.
     *
     * @param argName The name of the argument to check.
     * @return True if the argument is required, false otherwise.
     */
    public boolean required(@NotNull String argName) {
        return argRequiredMap.getOrDefault(argName, false);
    }

    /**
     * Retrieves the required number of arguments.
     *
     * @return The required number of arguments.
     */
    public int length() {
        return argRequiredMap.size();
    }

    /**
     * Checks if there are any arguments.
     *
     * @return True if there are arguments, false otherwise.
     */
    public boolean hasArgs() {
        return !args.isEmpty();
    }
}