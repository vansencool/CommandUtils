package dev.vansen.commandutils.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

/**
 * This is generally not recommended to use, since it limits the ability to nest arguments, and it is generally recommended to use {@link SimpleCommandArgument} since it allows easier command argument management, and saves alot of time in coding.
 */
@SuppressWarnings("unused")
public abstract class AbstractCommandArgument {

    private final Argument argument;
    private CompletionHandler handler;

    public AbstractCommandArgument(@NotNull Argument argument) {
        this.argument = argument;
    }

    public AbstractCommandArgument(@NotNull String name, @NotNull ArgumentType<?> type) {
        this(new Argument(name, type));
    }

    public AbstractCommandArgument(@NotNull Argument argument, @NotNull CompletionHandler handler) {
        this.argument = argument;
        this.handler = handler;
    }

    public AbstractCommandArgument(@NotNull String name, @NotNull ArgumentType<?> type, CompletionHandler handler) {
        this(new Argument(name, type), handler);
    }

    public SenderTypes[] senderTypes() {
        return null;
    }

    public void execute(@NotNull CommandWrapper context) {
    }

    public void playerExecute(@NotNull CommandWrapper context) {
    }

    public void consoleExecute(@NotNull CommandWrapper context) {
    }

    public void remoteConsoleExecute(@NotNull CommandWrapper context) {
    }

    public void entityExecute(@NotNull CommandWrapper context) {
    }

    public void blockExecute(@NotNull CommandWrapper context) {
    }

    public void proxiedExecute(@NotNull CommandWrapper context) {
    }

    public CommandPermission permission() {
        return null;
    }

    public List<CommandArgument> arguments() {
        return List.of();
    }

    public List<AbstractCommandArgument> abstractArguments() {
        return List.of();
    }

    public List<SubCommand> subCommands() {
        return List.of();
    }

    public List<AbstractSubCommand> abstractSubCommands() {
        return List.of();
    }

    @NotNull
    public Position argumentPosition() {
        return Position.LAST; // adds argument at the last.
    }

    @NotNull
    public Position abstractArgumentPosition() {
        return Position.LAST; // adds abstract arguments at the last.
    }

    @NotNull
    public Position abstractSubCommandPosition() {
        return Position.LAST; // adds abstract subcommands at the last.
    }

    public CommandArgument build() {
        CommandArgument arg = CommandArgument.of(argument);

        try {
            Method executeMethod = this.getClass().getDeclaredMethod("execute", CommandWrapper.class);
            Method superExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("execute", CommandWrapper.class);
            if (!executeMethod.equals(superExecuteMethod)) {
                if (senderTypes() != null) {
                    arg.defaultExecute(this::execute, ExecutableSender.types(senderTypes()));
                } else {
                    arg.defaultExecute(this::execute);
                }
            }

            Method playerExecuteMethod = this.getClass().getDeclaredMethod("playerExecute", CommandWrapper.class);
            Method superPlayerExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("playerExecute", CommandWrapper.class);
            if (!playerExecuteMethod.equals(superPlayerExecuteMethod)) {
                arg.playerExecute(this::playerExecute);
            }

            Method consoleExecuteMethod = this.getClass().getDeclaredMethod("consoleExecute", CommandWrapper.class);
            Method superConsoleExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("consoleExecute", CommandWrapper.class);
            if (!consoleExecuteMethod.equals(superConsoleExecuteMethod)) {
                arg.consoleExecute(this::consoleExecute);
            }

            Method remoteConsoleExecuteMethod = this.getClass().getDeclaredMethod("remoteConsoleExecute", CommandWrapper.class);
            Method superRemoteConsoleExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("remoteConsoleExecute", CommandWrapper.class);
            if (!remoteConsoleExecuteMethod.equals(superRemoteConsoleExecuteMethod)) {
                arg.remoteConsoleExecute(this::remoteConsoleExecute);
            }

            Method entityExecuteMethod = this.getClass().getDeclaredMethod("entityExecute", CommandWrapper.class);
            Method superEntityExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("entityExecute", CommandWrapper.class);
            if (!entityExecuteMethod.equals(superEntityExecuteMethod)) {
                arg.entityExecute(this::entityExecute);
            }

            Method blockExecuteMethod = this.getClass().getDeclaredMethod("blockExecute", CommandWrapper.class);
            Method superBlockExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("blockExecute", CommandWrapper.class);
            if (!blockExecuteMethod.equals(superBlockExecuteMethod)) {
                arg.blockExecute(this::blockExecute);
            }

            Method proxiedExecuteMethod = this.getClass().getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            Method superProxiedExecuteMethod = AbstractCommandArgument.class.getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            if (!proxiedExecuteMethod.equals(superProxiedExecuteMethod)) {
                arg.proxiedExecute(this::proxiedExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        if (permission() != null) arg.permission(permission());

        if (handler != null) arg.completion(handler);

        if (argumentPosition() == Position.LAST) {
            if (abstractSubCommandPosition() == Position.LAST) {
                subCommands().forEach(arg::subCommand);
                abstractSubCommands().forEach(arg::subCommand);
            } else {
                abstractSubCommands().forEach(arg::subCommand);
                subCommands().forEach(arg::subCommand);
            }
            if (abstractArgumentPosition() == Position.LAST) {
                arguments().forEach(arg::argument);
                abstractArguments().forEach(arg::argument);
            } else {
                abstractArguments().forEach(arg::argument);
                arguments().forEach(arg::argument);
            }
        } else {
            if (abstractArgumentPosition() == Position.LAST) {
                arguments().forEach(arg::argument);
                abstractArguments().forEach(arg::argument);
            } else {
                abstractArguments().forEach(arg::argument);
                arguments().forEach(arg::argument);
            }
            if (abstractSubCommandPosition() == Position.LAST) {
                subCommands().forEach(arg::subCommand);
                abstractSubCommands().forEach(arg::subCommand);
            } else {
                abstractSubCommands().forEach(arg::subCommand);
                subCommands().forEach(arg::subCommand);
            }
        }
        return arg;
    }
}