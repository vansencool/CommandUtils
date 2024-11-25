package dev.vansen.commandutils;

import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.info.Aliases;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractCommandUtils {
    private final String name;
    private Aliases aliases;
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;

    public AbstractCommandUtils(@NotNull String commandName) {
        this.name = commandName;
    }

    public AbstractCommandUtils(@NotNull String commandName, @NotNull Aliases aliases) {
        this.name = commandName;
        this.aliases = aliases;
    }

    public AbstractCommandUtils(@NotNull String commandName, @NotNull List<String> aliases) {
        this.name = commandName;
        this.aliases = Aliases.of(aliases);
    }

    public AbstractCommandUtils(@NotNull String commandName, @NotNull String... aliases) {
        this.name = commandName;
        this.aliases = Aliases.of(aliases);
    }

    public void execute(@NotNull CommandWrapper context) {
    }

    public SenderTypes[] senderTypes() {
        return null;
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

    public CommandInfo info() {
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
        return Position.LAST; // Adds argument at the last.
    }

    @NotNull
    public Position abstractArgumentPosition() {
        return Position.LAST; // Adds abstract arguments at the last.
    }

    @NotNull
    public Position abstractSubCommandPosition() {
        return Position.LAST; // Adds abstract subcommands at the last.
    }

    @ApiStatus.NonExtendable
    public CommandUtils build() {
        CommandUtils commandUtils = CommandUtils.command(name);

        try {
            Method executeMethod = this.getClass().getDeclaredMethod("execute", CommandWrapper.class);
            Method superExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("execute", CommandWrapper.class);
            if (!executeMethod.equals(superExecuteMethod)) {
                if (senderTypes() != null) {
                    commandUtils.defaultExecute(this::execute, ExecutableSender.types(senderTypes()));
                } else {
                    commandUtils.defaultExecute(this::execute);
                }
            }

            Method playerExecuteMethod = this.getClass().getDeclaredMethod("playerExecute", CommandWrapper.class);
            Method superPlayerExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("playerExecute", CommandWrapper.class);
            if (!playerExecuteMethod.equals(superPlayerExecuteMethod)) {
                commandUtils.playerExecute(this::playerExecute);
            }

            Method consoleExecuteMethod = this.getClass().getDeclaredMethod("consoleExecute", CommandWrapper.class);
            Method superConsoleExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("consoleExecute", CommandWrapper.class);
            if (!consoleExecuteMethod.equals(superConsoleExecuteMethod)) {
                commandUtils.consoleExecute(this::consoleExecute);
            }

            Method remoteConsoleExecuteMethod = this.getClass().getDeclaredMethod("remoteConsoleExecute", CommandWrapper.class);
            Method superRemoteConsoleExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("remoteConsoleExecute", CommandWrapper.class);
            if (!remoteConsoleExecuteMethod.equals(superRemoteConsoleExecuteMethod)) {
                commandUtils.remoteConsoleExecute(this::remoteConsoleExecute);
            }

            Method entityExecuteMethod = this.getClass().getDeclaredMethod("entityExecute", CommandWrapper.class);
            Method superEntityExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("entityExecute", CommandWrapper.class);
            if (!entityExecuteMethod.equals(superEntityExecuteMethod)) {
                commandUtils.entityExecute(this::entityExecute);
            }

            Method blockExecuteMethod = this.getClass().getDeclaredMethod("blockExecute", CommandWrapper.class);
            Method superBlockExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("blockExecute", CommandWrapper.class);
            if (!blockExecuteMethod.equals(superBlockExecuteMethod)) {
                commandUtils.blockExecute(this::blockExecute);
            }

            Method proxiedExecuteMethod = this.getClass().getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            Method superProxiedExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            if (!proxiedExecuteMethod.equals(superProxiedExecuteMethod)) {
                commandUtils.proxiedExecute(this::proxiedExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        if (info() != null) commandUtils.info(info());
        if (aliases != null) commandUtils.aliases(aliases);

        if (argumentPosition() == Position.LAST) {
            if (abstractSubCommandPosition() == Position.LAST) {
                subCommands().forEach(commandUtils::subCommand);
                abstractSubCommands().forEach(commandUtils::subCommand);
            } else {
                abstractSubCommands().forEach(commandUtils::subCommand);
                subCommands().forEach(commandUtils::subCommand);
            }
            if (abstractArgumentPosition() == Position.LAST) {
                arguments().forEach(commandUtils::argument);
                abstractArguments().forEach(commandUtils::argument);
            } else {
                abstractArguments().forEach(commandUtils::argument);
                arguments().forEach(commandUtils::argument);
            }
        } else {
            if (abstractArgumentPosition() == Position.LAST) {
                arguments().forEach(commandUtils::argument);
                abstractArguments().forEach(commandUtils::argument);
            } else {
                abstractArguments().forEach(commandUtils::argument);
                arguments().forEach(commandUtils::argument);
            }
            if (abstractSubCommandPosition() == Position.LAST) {
                subCommands().forEach(commandUtils::subCommand);
                abstractSubCommands().forEach(commandUtils::subCommand);
            } else {
                abstractSubCommands().forEach(commandUtils::subCommand);
                subCommands().forEach(commandUtils::subCommand);
            }
        }

        return commandUtils;
    }

    @ApiStatus.NonExtendable
    public void register() {
        build().register(); // easier
    }
}
