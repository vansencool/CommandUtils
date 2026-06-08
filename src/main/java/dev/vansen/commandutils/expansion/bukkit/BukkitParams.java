package dev.vansen.commandutils.expansion.bukkit;

import org.jetbrains.annotations.NotNull;

/**
 * A utility class for building parameter strings for Bukkit commands.
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class BukkitParams {
    private final StringBuilder args = new StringBuilder();

    /**
     * Creates a new instance of BukkitParams.
     *
     * @return a new instance of BukkitParams
     */
    public static BukkitParams of() {
        return new BukkitParams();
    }

    /**
     * Creates a new instance of BukkitParams with the specified arguments.
     * Please do not have a space at the end of the arguments.
     *
     * @param args the arguments to append
     * @return a new instance of BukkitParams
     * @see #ofWithSpace(String)
     */
    public static BukkitParams of(@NotNull String args) {
        BukkitParams params = new BukkitParams();
        params.args.append(args).append(" ");
        return params;
    }

    /**
     * Creates a new instance of BukkitParams with the specified arguments.
     * Please do have a space at the end of the arguments.
     *
     * @param args the arguments to append
     * @return a new instance of BukkitParams
     * @see #of(String)
     */
    public static BukkitParams ofWithSpace(@NotNull String args) {
        BukkitParams params = new BukkitParams();
        params.args.append(args);
        return params;
    }

    /**
     * Creates a new instance of BukkitParams with the specified arguments.
     *
     * @param args the arguments to append
     * @return a new instance of BukkitParams
     */
    public static BukkitParams of(@NotNull String... args) {
        return new BukkitParams().params(args);
    }

    /**
     * Creates a new instance of BukkitParams with the specified arguments.
     *
     * @param required whether the arguments are required
     * @param args     the arguments to append
     * @return a new instance of BukkitParams
     */
    public static BukkitParams of(boolean required, @NotNull String... args) {
        return new BukkitParams().params(required, args);
    }

    /**
     * Appends a required parameter to the parameter string.
     *
     * @param param the name of the required parameter
     * @return this instance for chaining
     */
    public BukkitParams required(@NotNull String param) {
        args.append("<").append(param).append(">").append(" ");
        return this;
    }

    /**
     * Appends an optional parameter to the parameter string.
     *
     * @param param the name of the optional parameter
     * @return this instance for chaining
     */
    public BukkitParams optional(@NotNull String param) {
        args.append("[").append(param).append("]").append(" ");
        return this;
    }

    /**
     * Appends a parameter to the parameter string with the specified requirement.
     *
     * @param param    the name of the parameter
     * @param required whether the parameter is required
     * @return this instance for chaining
     */
    public BukkitParams param(@NotNull String param, boolean required) {
        if (required) required(param);
        else optional(param);
        return this;
    }

    /**
     * Appends a required parameter to the parameter string.
     *
     * @param param the name of the required parameter
     * @return this instance for chaining
     */
    public BukkitParams param(@NotNull String param) {
        return required(param);
    }

    /**
     * Appends multiple parameters to the parameter string.
     *
     * @param params the names of the parameters
     * @return this instance for chaining
     */
    public BukkitParams params(@NotNull String... params) {
        for (String param : params) {
            required(param);
        }
        return this;
    }

    /**
     * Appends multiple parameters to the parameter string.
     *
     * @param required whether the parameters are required
     * @param params   the names of the parameters
     * @return this instance for chaining
     */
    public BukkitParams params(boolean required, @NotNull String... params) {
        if (required) {
            for (String param : params) {
                required(param);
            }
        } else {
            for (String param : params) {
                optional(param);
            }
        }
        return this;
    }

    /**
     * Returns the built parameter string.
     *
     * @return the parameter string
     */
    public String args() {
        return args.toString();
    }
}