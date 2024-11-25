package dev.vansen.commandutils.subcommand;

import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a subcommand in a command.
 * Subcommands are individual command branches with their own execution logic and arguments.
 */
@SuppressWarnings("unused")
public class SimpleSubCommand {
    private final String name;
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor remoteConsoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;
    private CommandPermission permission;
    private List<CommandArgument> arguments;
    private List<AbstractCommandArgument> abstractArguments;
    private List<SubCommand> subCommands;
    private List<AbstractSubCommand> abstractSubCommands;
    private Position argumentPosition;
    private Position abstractArgumentPosition;
    private Position abstractSubCommandPosition;
    private SenderTypes[] senderTypes;

    /**
     * Constructs a new SimpleSubCommand with the specified name.
     *
     * @param name the name of the subcommand.
     */
    public SimpleSubCommand(@NotNull String name) {
        this.name = name;
    }

    /**
     * Sets the default executor for this subcommand.
     * This executor is used if no other specific executor is matched for the sender type.
     *
     * @param executor the {@link CommandExecutor} to be executed by default.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand execute(@NotNull CommandExecutor executor) {
        this.defaultExecutor = executor;
        return this;
    }

    /**
     * Sets the default executor for this subcommand, along with the sender types it applies to.
     *
     * @param executor    the {@link CommandExecutor} to be executed by default.
     * @param senderTypes the {@link SenderTypes} that the executor applies to.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    public SimpleSubCommand execute(@NotNull CommandExecutor executor, SenderTypes... senderTypes) {
        this.defaultExecutor = executor;
        this.senderTypes = senderTypes;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link Player}.
     *
     * @param executor the {@link CommandExecutor} to be executed for players.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand playerExecute(@NotNull CommandExecutor executor) {
        this.playerExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link ConsoleCommandSender}.
     *
     * @param executor the {@link CommandExecutor} to be executed for console senders.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand consoleExecute(@NotNull CommandExecutor executor) {
        this.consoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link org.bukkit.command.RemoteConsoleCommandSender}.
     *
     * @param executor the {@link CommandExecutor} to be executed for console senders.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand remoteConsoleExecute(@NotNull CommandExecutor executor) {
        this.remoteConsoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is an {@link Entity}.
     *
     * @param executor the {@link CommandExecutor} to be executed for entities.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand entityExecute(@NotNull CommandExecutor executor) {
        this.entityExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link BlockCommandSender}.
     *
     * @param executor the {@link CommandExecutor} to be executed for block senders.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand blockExecute(@NotNull CommandExecutor executor) {
        this.blockExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a proxied sender.
     *
     * @param executor the {@link CommandExecutor} to be executed for proxied senders.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand proxiedExecute(@NotNull CommandExecutor executor) {
        this.proxiedExecutor = executor;
        return this;
    }

    /**
     * Sets the permission required for executing this subcommand.
     *
     * @param permission the {@link CommandPermission} required for executing this subcommand.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand permission(@NotNull CommandPermission permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Sets the arguments for this subcommand.
     *
     * @param arguments the list of {@link CommandArgument} for this subcommand.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand arguments(@NotNull List<CommandArgument> arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * Sets the abstract arguments for this subcommand.
     *
     * @param abstractArguments the list of {@link AbstractCommandArgument} for this subcommand.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand abstractArguments(@NotNull List<AbstractCommandArgument> abstractArguments) {
        this.abstractArguments = abstractArguments;
        return this;
    }

    /**
     * Sets the subcommands for this subcommand.
     *
     * @param subCommands the list of {@link SubCommand} to be used as subcommands.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand subCommands(@NotNull List<SubCommand> subCommands) {
        this.subCommands = subCommands;
        return this;
    }

    /**
     * Sets the abstract subcommands for this subcommand.
     *
     * @param abstractSubCommands the list of {@link AbstractSubCommand} to be used as abstract subcommands.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand abstractSubCommands(@NotNull List<AbstractSubCommand> abstractSubCommands) {
        this.abstractSubCommands = abstractSubCommands;
        return this;
    }

    /**
     * Sets the position of the arguments for this subcommand.
     *
     * @param position the {@link Position} of the arguments.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand argumentPosition(@NotNull Position position) {
        this.argumentPosition = position;
        return this;
    }

    /**
     * Sets the position of the abstract arguments for this subcommand.
     *
     * @param position the {@link Position} of the abstract arguments.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand abstractArgumentPosition(@NotNull Position position) {
        this.abstractArgumentPosition = position;
        return this;
    }

    /**
     * Sets the position of the abstract subcommands for this subcommand.
     *
     * @param position the {@link Position} of the abstract subcommands.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand abstractSubCommandPosition(@NotNull Position position) {
        this.abstractSubCommandPosition = position;
        return this;
    }

    /**
     * Sets the sender types allowed to execute this subcommand.
     *
     * @param senderTypes the allowed {@link SenderTypes} for this subcommand.
     * @return the current {@link SimpleSubCommand} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleSubCommand senderTypes(SenderTypes... senderTypes) {
        this.senderTypes = senderTypes;
        return this;
    }

    /**
     * Builds and returns a {@link SubCommand} with the specified configurations set on this {@link SimpleSubCommand}.
     *
     * @return a fully configured {@link SubCommand} instance.
     */
    @ApiStatus.NonExtendable
    public SubCommand build() {
        SubCommand subCommand = SubCommand.of(name);

        if (senderTypes != null) subCommand.senderTypes(ExecutableSender.types(senderTypes));
        if (defaultExecutor != null) subCommand.defaultExecute(defaultExecutor);
        if (playerExecutor != null) subCommand.playerExecute(playerExecutor);
        if (consoleExecutor != null) subCommand.consoleExecute(consoleExecutor);
        if (remoteConsoleExecutor != null) subCommand.remoteConsoleExecute(remoteConsoleExecutor);
        if (entityExecutor != null) subCommand.entityExecute(entityExecutor);
        if (blockExecutor != null) subCommand.blockExecute(blockExecutor);
        if (proxiedExecutor != null) subCommand.proxiedExecute(proxiedExecutor);

        if (permission != null) subCommand.permission(permission);

        if (argumentPosition == Position.LAST) {
            if (abstractSubCommandPosition == Position.LAST) {
                if (subCommands != null) subCommands.forEach(subCommand::subCommand);
                if (abstractSubCommands != null) abstractSubCommands.forEach(subCommand::subCommand);
            } else {
                if (abstractSubCommands != null) abstractSubCommands.forEach(subCommand::subCommand);
                if (subCommands != null) subCommands.forEach(subCommand::subCommand);
            }
            if (abstractArgumentPosition == Position.LAST) {
                if (arguments != null) arguments.forEach(subCommand::argument);
                if (abstractArguments != null) abstractArguments.forEach(subCommand::argument);
            } else {
                if (abstractArguments != null) abstractArguments.forEach(subCommand::argument);
                if (arguments != null) arguments.forEach(subCommand::argument);
            }
        } else {
            if (abstractArgumentPosition == Position.LAST) {
                if (arguments != null) arguments.forEach(subCommand::argument);
                if (abstractArguments != null) abstractArguments.forEach(subCommand::argument);
            } else {
                if (abstractArguments != null) abstractArguments.forEach(subCommand::argument);
                if (arguments != null) arguments.forEach(subCommand::argument);
            }
            if (abstractSubCommandPosition == Position.LAST) {
                if (subCommands != null) subCommands.forEach(subCommand::subCommand);
                if (abstractSubCommands != null) abstractSubCommands.forEach(subCommand::subCommand);
            } else {
                if (abstractSubCommands != null) abstractSubCommands.forEach(subCommand::subCommand);
                if (subCommands != null) subCommands.forEach(subCommand::subCommand);
            }
        }

        return subCommand;
    }

    /**
     * Gets the name of this subcommand.
     *
     * @return the name of this subcommand.
     */
    @ApiStatus.NonExtendable
    public String name() {
        return name;
    }
}