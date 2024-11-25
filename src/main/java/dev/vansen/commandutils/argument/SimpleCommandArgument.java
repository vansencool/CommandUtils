package dev.vansen.commandutils.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a command argument in the command system.
 * Arguments are individual command branches with their own execution logic and other things.
 */
@SuppressWarnings("unused")
public class SimpleCommandArgument {
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;
    private final Argument<?> argument;
    private CompletionHandler handler;
    private SenderTypes[] senderTypes;
    private CommandPermission permission;
    private Position argumentPosition = Position.LAST;
    private Position abstractArgumentPosition = Position.LAST;
    private Position abstractSubCommandPosition = Position.LAST;
    private List<CommandArgument> arguments;
    private List<AbstractCommandArgument> abstractArguments;
    private List<SubCommand> subCommands;
    private List<AbstractSubCommand> abstractSubCommands;

    /**
     * Constructs a new SimpleCommandArgument with the specified argument.
     *
     * @param argument the argument to be associated with this SimpleCommandArgument.
     */
    public <T> SimpleCommandArgument(@NotNull Argument<T> argument) {
        this.argument = argument;
    }

    /**
     * Constructs a new SimpleCommandArgument with the specified argument name and type.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     */
    public <T> SimpleCommandArgument(@NotNull String name, @NotNull ArgumentType<T> type) {
        this(new Argument<>(name, type));
    }

    /**
     * Constructs a new SimpleCommandArgument with the specified argument and completion handler.
     *
     * @param argument the argument to be associated with this SimpleCommandArgument.
     * @param handler  the completion handler to be associated with this SimpleCommandArgument.
     */
    public <T> SimpleCommandArgument(@NotNull Argument<T> argument, @NotNull CompletionHandler handler) {
        this.argument = argument;
        this.handler = handler;
    }

    /**
     * Constructs a new SimpleCommandArgument with the specified argument name, type, and completion handler.
     *
     * @param name    the name of the argument.
     * @param type    the type of the argument.
     * @param handler the completion handler to be associated with this SimpleCommandArgument.
     */
    public <T> SimpleCommandArgument(@NotNull String name, @NotNull ArgumentType<T> type, CompletionHandler handler) {
        this(new Argument<>(name, type), handler);
    }

    /**
     * Sets the default executor for this subcommand.
     * This executor is used if no other specific executor is matched for the sender type.
     *
     * @param executor the {@link CommandExecutor} to be executed by default.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument execute(@NotNull CommandExecutor executor) {
        this.defaultExecutor = executor;
        return this;
    }

    /**
     * Sets the default executor for this subcommand, along with the sender types it applies to.
     *
     * @param executor    the {@link CommandExecutor} to be executed by default.
     * @param senderTypes the {@link SenderTypes} that the executor applies to.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument execute(@NotNull CommandExecutor executor, SenderTypes... senderTypes) {
        this.defaultExecutor = executor;
        this.senderTypes = senderTypes;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link Player}.
     *
     * @param executor the {@link CommandExecutor} to be executed for players.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument playerExecute(@NotNull CommandExecutor executor) {
        this.playerExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link ConsoleCommandSender}.
     *
     * @param executor the {@link CommandExecutor} to be executed for console senders.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument consoleExecute(@NotNull CommandExecutor executor) {
        this.consoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is an {@link Entity}.
     *
     * @param executor the {@link CommandExecutor} to be executed for entities.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument entityExecute(@NotNull CommandExecutor executor) {
        this.entityExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a {@link BlockCommandSender}.
     *
     * @param executor the {@link CommandExecutor} to be executed for block senders.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument blockExecute(@NotNull CommandExecutor executor) {
        this.blockExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for this subcommand, but only if the sender is a proxied sender.
     *
     * @param executor the {@link CommandExecutor} to be executed for proxied senders.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument proxiedExecute(@NotNull CommandExecutor executor) {
        this.proxiedExecutor = executor;
        return this;
    }

    /**
     * Sets the sender types for this argument.
     *
     * @param senderTypes the {@link SenderTypes} that this argument applies to.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument senderTypes(SenderTypes... senderTypes) {
        this.senderTypes = senderTypes;
        return this;
    }

    /**
     * Sets the permission required for executing this argument.
     *
     * @param permission the {@link CommandPermission} required for executing this argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument permission(@NotNull CommandPermission permission) {
        this.permission = permission;
        return this;
    }

    /**
     * Sets the argument position in the command.
     *
     * @param position the position of the argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument argumentPosition(Position position) {
        this.argumentPosition = position;
        return this;
    }

    /**
     * Sets the abstract argument position in the command.
     *
     * @param position the position of the abstract argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument abstractArgumentPosition(Position position) {
        this.abstractArgumentPosition = position;
        return this;
    }

    /**
     * Sets the abstract subcommand position in the command.
     *
     * @param position the position of the abstract subcommand.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument abstractSubCommandPosition(Position position) {
        this.abstractSubCommandPosition = position;
        return this;
    }

    /**
     * Sets the completion handler for this argument.
     *
     * @param handler the {@link CompletionHandler} for this argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument completionHandler(CompletionHandler handler) {
        this.handler = handler;
        return this;
    }

    /**
     * Sets the subcommands for this argument.
     *
     * @param subCommands the list of {@link SubCommand} for this argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument subCommands(@NotNull List<SubCommand> subCommands) {
        this.subCommands = subCommands;
        return this;
    }

    /**
     * Sets the abstract subcommands for this argument.
     *
     * @param abstractSubCommands the list of {@link AbstractSubCommand} for this argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument abstractSubCommands(@NotNull List<AbstractSubCommand> abstractSubCommands) {
        this.abstractSubCommands = abstractSubCommands;
        return this;
    }

    /**
     * Sets the arguments for this argument.
     *
     * @param arguments the list of {@link CommandArgument} for this argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument arguments(@NotNull List<CommandArgument> arguments) {
        this.arguments = arguments;
        return this;
    }

    /**
     * Sets the abstract arguments for this argument.
     *
     * @param abstractArguments the list of {@link AbstractCommandArgument} for this argument.
     * @return the current {@link SimpleCommandArgument} instance for chaining.
     */
    @ApiStatus.NonExtendable
    public SimpleCommandArgument abstractArguments(@NotNull List<AbstractCommandArgument> abstractArguments) {
        this.abstractArguments = abstractArguments;
        return this;
    }

    /**
     * Builds and returns the {@link CommandArgument} based on the configuration.
     *
     * @return the constructed {@link CommandArgument}.
     */
    @ApiStatus.NonExtendable
    public CommandArgument build() {
        CommandArgument arg = CommandArgument.of(argument);

        if (senderTypes != null) arg.senderTypes(ExecutableSender.types(senderTypes));
        if (defaultExecutor != null) arg.defaultExecute(defaultExecutor);
        if (playerExecutor != null) arg.playerExecute(playerExecutor);
        if (consoleExecutor != null) arg.consoleExecute(consoleExecutor);
        if (entityExecutor != null) arg.entityExecute(entityExecutor);
        if (blockExecutor != null) arg.blockExecute(blockExecutor);
        if (proxiedExecutor != null) arg.proxiedExecute(proxiedExecutor);

        if (handler != null) arg.completion(handler);

        if (permission != null) arg.permission(permission);

        if (argumentPosition == Position.LAST) {
            if (abstractSubCommandPosition == Position.LAST) {
                if (subCommands != null) subCommands.forEach(arg::subCommand);
                if (abstractSubCommands != null) abstractSubCommands.forEach(arg::subCommand);
            } else {
                if (abstractSubCommands != null) abstractSubCommands.forEach(arg::subCommand);
                if (subCommands != null) subCommands.forEach(arg::subCommand);
            }
            if (abstractArgumentPosition == Position.LAST) {
                if (arguments != null) arguments.forEach(arg::argument);
                if (abstractArguments != null) abstractArguments.forEach(arg::argument);
            } else {
                if (abstractArguments != null) abstractArguments.forEach(arg::argument);
                if (arguments != null) arguments.forEach(arg::argument);
            }
        } else {
            if (abstractArgumentPosition == Position.LAST) {
                if (arguments != null) arguments.forEach(arg::argument);
                if (abstractArguments != null) abstractArguments.forEach(arg::argument);
            } else {
                if (abstractArguments != null) abstractArguments.forEach(arg::argument);
                if (arguments != null) arguments.forEach(arg::argument);
            }
            if (abstractSubCommandPosition == Position.LAST) {
                if (subCommands != null) subCommands.forEach(arg::subCommand);
                if (abstractSubCommands != null) abstractSubCommands.forEach(arg::subCommand);
            } else {
                if (abstractSubCommands != null) abstractSubCommands.forEach(arg::subCommand);
                if (subCommands != null) subCommands.forEach(arg::subCommand);
            }
        }

        return arg;
    }

    /**
     * Gets the argument.
     *
     * @return the argument.
     */
    @NotNull
    @ApiStatus.NonExtendable
    public Argument<?> argument() {
        return argument;
    }
}