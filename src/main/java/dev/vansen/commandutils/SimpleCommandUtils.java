package dev.vansen.commandutils;

import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.info.Aliases;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A utility class for simplifying the creation, customization, and registration of commands.
 * Supports specifying executors for various sender types, defining arguments, aliases, subcommands, and more.
 */
@SuppressWarnings("unused")
public class SimpleCommandUtils {

    private final String name;
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor remoteConsoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;
    private Aliases aliases;
    private CommandInfo info;
    private SenderTypes[] sender;
    private List<CommandArgument> arguments;
    private List<AbstractCommandArgument> abstractArguments;
    private List<SubCommand> subCommands;
    private List<AbstractSubCommand> abstractSubCommands;
    private Position argumentPosition;
    private Position abstractArgumentPosition;
    private Position abstractSubCommandPosition;

    /**
     * Constructs a SimpleCommandUtils instance with a command name.
     *
     * @param name the name of the command.
     */
    public SimpleCommandUtils(@NotNull String name) {
        this.name = name;
    }

    /**
     * Constructs a SimpleCommandUtils instance with a command name and aliases.
     *
     * @param commandName the name of the command.
     * @param aliases     the aliases for the command.
     */
    public SimpleCommandUtils(@NotNull String commandName, @NotNull Aliases aliases) {
        this.name = commandName;
        this.aliases = aliases;
    }

    /**
     * Constructs a SimpleCommandUtils instance with a command name and aliases list.
     *
     * @param commandName the name of the command.
     * @param aliases     a list of aliases for the command.
     */
    public SimpleCommandUtils(@NotNull String commandName, @NotNull List<String> aliases) {
        this.name = commandName;
        this.aliases = Aliases.of(aliases);
    }

    /**
     * Constructs a SimpleCommandUtils instance with a command name and aliases array.
     *
     * @param commandName the name of the command.
     * @param aliases     an array of aliases for the command.
     */
    public SimpleCommandUtils(@NotNull String commandName, @NotNull String... aliases) {
        this.name = commandName;
        this.aliases = Aliases.of(aliases);
    }

    /**
     * Sets the default executor for the command.
     *
     * @param executor the default executor.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils execute(@NotNull CommandExecutor executor) {
        this.defaultExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for player senders.
     *
     * @param executor the executor for players.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils playerExecute(@NotNull CommandExecutor executor) {
        this.playerExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for console senders.
     *
     * @param executor the executor for the console.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils consoleExecute(@NotNull CommandExecutor executor) {
        this.consoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for remote console senders.
     *
     * @param executor the executor for remote consoles.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils remoteConsoleExecute(@NotNull CommandExecutor executor) {
        this.remoteConsoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for entity senders.
     *
     * @param executor the executor for entities.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils entityExecute(@NotNull CommandExecutor executor) {
        this.entityExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for block senders.
     *
     * @param executor the executor for blocks.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils blockExecute(@NotNull CommandExecutor executor) {
        this.blockExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for proxied senders.
     *
     * @param executor the executor for proxied senders.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils proxiedExecute(@NotNull CommandExecutor executor) {
        this.proxiedExecutor = executor;
        return this;
    }

    /**
     * Sets the aliases for the command.
     *
     * @param aliases the command aliases.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils aliases(@NotNull Aliases aliases) {
        this.aliases = aliases;
        return this;
    }

    /**
     * Sets the information for the command.
     *
     * @param info the command info.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils info(@NotNull CommandInfo info) {
        this.info = info;
        return this;
    }

    /**
     * Specifies the allowed sender types for the command.
     *
     * @param senderTypes the sender types.
     * @return the current instance for chaining.
     */
    public SimpleCommandUtils senderTypes(@NotNull SenderTypes... senderTypes) {
        this.sender = senderTypes;
        return this;
    }

    /**
     * Adds arguments to the command.
     *
     * @param arguments a list of command arguments.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils arguments(@NotNull List<CommandArgument> arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * Adds abstract arguments to the command.
     *
     * @param abstractArguments a list of abstract command arguments.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils abstractArguments(@NotNull List<AbstractCommandArgument> abstractArguments) {
        this.abstractArguments = abstractArguments;
        return this;
    }

    /**
     * Adds subcommands to the command.
     *
     * @param subCommands a list of subcommands.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils subCommands(@NotNull List<SubCommand> subCommands) {
        this.subCommands = subCommands;
        return this;
    }

    /**
     * Adds abstract subcommands to the command.
     *
     * @param abstractSubCommands a list of abstract subcommands.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils abstractSubCommands(@NotNull List<AbstractSubCommand> abstractSubCommands) {
        this.abstractSubCommands = abstractSubCommands;
        return this;
    }

    /**
     * Sets the position of command arguments.
     *
     * @param position the argument position.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils argumentPosition(@NotNull Position position) {
        this.argumentPosition = position;
        return this;
    }

    /**
     * Sets the position of abstract command arguments.
     *
     * @param position the abstract argument position.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils abstractArgumentPosition(@NotNull Position position) {
        this.abstractArgumentPosition = position;
        return this;
    }

    /**
     * Sets the position of abstract subcommands.
     *
     * @param position the abstract subcommand position.
     * @return the current instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandUtils abstractSubCommandPosition(@NotNull Position position) {
        this.abstractSubCommandPosition = position;
        return this;
    }

    /**
     * Builds a {@link CommandUtils} instance based on the current configuration.
     *
     * @return a fully constructed CommandUtils object.
     */
    @ApiStatus.NonExtendable
    public CommandUtils build() {
        CommandUtils commandUtils = CommandUtils.command(name);

        if (sender != null) commandUtils.senderTypes(ExecutableSender.types(sender));
        if (defaultExecutor != null) commandUtils.defaultExecute(defaultExecutor);
        if (playerExecutor != null) commandUtils.playerExecute(playerExecutor);
        if (consoleExecutor != null) commandUtils.consoleExecute(consoleExecutor);
        if (remoteConsoleExecutor != null) commandUtils.remoteConsoleExecute(remoteConsoleExecutor);
        if (entityExecutor != null) commandUtils.entityExecute(entityExecutor);
        if (blockExecutor != null) commandUtils.blockExecute(blockExecutor);
        if (proxiedExecutor != null) commandUtils.proxiedExecute(proxiedExecutor);

        if (aliases != null) commandUtils.aliases(aliases);
        if (info != null) commandUtils.info(info);

        if (argumentPosition == Position.LAST) {
            if (abstractSubCommandPosition == Position.LAST) {
                if (subCommands != null) subCommands.forEach(commandUtils::subCommand);
                if (abstractSubCommands != null) abstractSubCommands.forEach(commandUtils::subCommand);
            } else {
                if (abstractSubCommands != null) abstractSubCommands.forEach(commandUtils::subCommand);
                if (subCommands != null) subCommands.forEach(commandUtils::subCommand);
            }
            if (abstractArgumentPosition == Position.LAST) {
                if (arguments != null) arguments.forEach(commandUtils::argument);
                if (abstractArguments != null) abstractArguments.forEach(commandUtils::argument);
            } else {
                if (abstractArguments != null) abstractArguments.forEach(commandUtils::argument);
                if (arguments != null) arguments.forEach(commandUtils::argument);
            }
        } else {
            if (abstractArgumentPosition == Position.LAST) {
                if (arguments != null) arguments.forEach(commandUtils::argument);
                if (abstractArguments != null) abstractArguments.forEach(commandUtils::argument);
            } else {
                if (abstractArguments != null) abstractArguments.forEach(commandUtils::argument);
                if (arguments != null) arguments.forEach(commandUtils::argument);
            }
            if (abstractSubCommandPosition == Position.LAST) {
                if (subCommands != null) subCommands.forEach(commandUtils::subCommand);
                if (abstractSubCommands != null) abstractSubCommands.forEach(commandUtils::subCommand);
            } else {
                if (abstractSubCommands != null) abstractSubCommands.forEach(commandUtils::subCommand);
                if (subCommands != null) subCommands.forEach(commandUtils::subCommand);
            }
        }

        return commandUtils;
    }

    /**
     * Registers the built command with the system.
     */
    public void register() {
        build().register();
    }
}