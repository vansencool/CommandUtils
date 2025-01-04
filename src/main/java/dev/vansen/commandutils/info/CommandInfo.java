package dev.vansen.commandutils.info;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dev.vansen.commandutils.permission.CommandPermission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Provides information about a command, including its description, aliases, and required permissions.
 * This class is used to configure and retrieve metadata about commands.
 */
@SuppressWarnings("unused")
public final class CommandInfo {
    private @Nullable String description;
    private @Nullable List<String> aliases;
    private @Nullable CommandPermission permission;

    /**
     * Creates a new {@link CommandInfo} instance.
     *
     * @return a new {@link CommandInfo} instance.
     */
    @NotNull
    public static CommandInfo info() {
        return new CommandInfo();
    }

    /**
     * Sets the description of the command.
     *
     * @param description the description of the command.
     * @return this {@link CommandInfo} instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo description(@Nullable String description) {
        if (description == null) return this;
        this.description = description;
        return this;
    }

    /**
     * Sets the aliases of the command using an array of strings.
     *
     * @param aliases the aliases of the command.
     * @return this {@link CommandInfo} instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(@Nullable String... aliases) {
        if (aliases == null) return this;
        this.aliases = List.of(aliases);
        return this;
    }

    /**
     * Sets the aliases of the command using a list of strings.
     *
     * @param aliases the list of aliases for the command.
     * @return this {@link CommandInfo} instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(@Nullable List<String> aliases) {
        if (aliases == null) return this;
        this.aliases = aliases;
        return this;
    }

    /**
     * Sets the aliases of the command using an {@link Aliases} instance.
     *
     * @param aliases the aliases of the command.
     * @return this {@link CommandInfo} instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo aliases(@Nullable Aliases aliases) {
        if (aliases == null) return this;
        this.aliases = aliases.getAliases();
        return this;
    }

    /**
     * Sets the required permission for the command.
     *
     * @param permission the {@link CommandPermission} required to execute the command.
     * @return this {@link CommandInfo} instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permission(@Nullable CommandPermission permission) {
        if (permission == null) return this;
        this.permission = permission;
        return this;
    }

    /**
     * Sets the required permission for the command.
     *
     * @param permission the {@link CommandPermission} required to execute the command.
     * @return this {@link CommandInfo} instance for method chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandInfo permission(@Nullable String permission) {
        if (permission == null) return this;
        this.permission = CommandPermission.permission(permission);
        return this;
    }

    /**
     * Gets the description of the command.
     *
     * @return the description of the command, or {@code null} if not set.
     */
    @Nullable
    public String getDescription() {
        return description;
    }

    /**
     * Gets the list of aliases for the command.
     *
     * @return the list of aliases, or {@code null} if not set.
     */
    @Nullable
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * Gets the required permission for the command.
     *
     * @return the {@link CommandPermission} required to execute the command, or {@code null} if not set.
     */
    @Nullable
    public CommandPermission getPermission() {
        return permission;
    }
}