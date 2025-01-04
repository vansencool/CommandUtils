package dev.vansen.commandutils.bukkit;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents information about a Bukkit command.
 */
@SuppressWarnings("unused")
public class BukkitInfo {
    private String name;
    private @Nullable String description;
    private @Nullable String permission;
    private @Nullable String[] aliases;
    private @NotNull String args = "";

    /**
     * Creates a new instance of BukkitInfo.
     *
     * @return a new instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public static BukkitInfo info() {
        return new BukkitInfo();
    }

    /**
     * Creates a new instance of BukkitInfo with the specified name.
     *
     * @param name the name of the command
     * @return a new instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public static BukkitInfo info(@NotNull String name) {
        return new BukkitInfo().name(name);
    }

    /**
     * Sets the name of the command.
     *
     * @param name the name of the command
     * @return this instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the description of the command.
     *
     * @param description the description of the command
     * @return this instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo description(@NotNull String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the permission required to execute the command.
     *
     * @param permission the permission required to execute the command
     * @return this instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo permission(@NotNull String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Sets the aliases for the command.
     *
     * @param aliases the aliases for the command
     * @return this instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo aliases(@NotNull String... aliases) {
        this.aliases = aliases;
        return this;
    }

    /**
     * Sets the aliases for the command.
     *
     * @param aliases the aliases for the command
     * @return this instance of BukkitInfo
     */

    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo aliases(@NotNull List<String> aliases) {
        this.aliases = aliases.toArray(new String[0]);
        return this;
    }

    /**
     * Sets the arguments for the command.
     * <p>
     * The format of the arguments is a string that describes the required and optional arguments of the command.
     * The format is as follows:
     * <p>
     * "&lt;required_arg_name&gt; [optional_arg_name]"
     * <p>
     * Where:
     * <ul>
     *     <li><code>required_arg_name</code> is the name of a required argument. This argument must be provided when the
     *     command is executed.</li>
     *     <li><code>optional_arg_name</code> is the name of an optional argument. This argument may be provided when the
     *     command is executed, but it is not required.</li>
     * </ul>
     * <p>
     * For example, if a command has two arguments, one required and one optional, the args string would be:
     * <p>
     * "&lt;required_arg_name&gt; [optional_arg_name]"
     * <p>
     * If a command has multiple required arguments, they should be separated by spaces:
     * <p>
     * "&lt;required_arg_name1&gt; &lt;required_arg_name2&gt; [optional_arg_name]"
     * <p>
     * If the command has no arguments, you can choose to not provide an args string.
     * <p>
     * This args string will be used further for argument parsing and validation. It is used to determine the expected
     * arguments of the command and to validate the actual arguments provided when the command is executed.
     * <p>
     * Also, you can use any argument name you want, just make sure not to conflict with other argument names.
     *
     * @param args the arguments for the command
     * @return this instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo args(@NotNull String args) {
        this.args = args;
        return this;
    }

    /**
     * Sets the arguments for the command using a BukkitParams instance.
     *
     * @param args the BukkitParams instance containing the arguments for the command
     * @return this instance of BukkitInfo
     */
    @NotNull
    @CanIgnoreReturnValue
    public BukkitInfo args(@NotNull BukkitParams args) {
        this.args = args.args();
        return this;
    }

    /**
     * Gets the name of the command.
     *
     * @return the name of the command
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * Gets the description of the command.
     *
     * @return the description of the command
     */
    @Nullable
    public String description() {
        return description;
    }

    /**
     * Gets the permission required to execute the command.
     *
     * @return the permission required to execute the command
     */
    @Nullable
    public String permission() {
        return permission;
    }

    /**
     * Gets the aliases for the command.
     *
     * @return the aliases for the command
     */
    @Nullable
    public String[] aliases() {
        return aliases;
    }

    /**
     * Gets the arguments for the command.
     *
     * @return the arguments for the command
     */
    @NotNull
    public String args() {
        return args;
    }
}