package dev.vansen.commandutils;

import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractCommandUtils {
    private final String name;
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;

    public AbstractCommandUtils(@NotNull String commandName) {
        this.name = commandName;
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
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method playerExecuteMethod = this.getClass().getDeclaredMethod("playerExecute", CommandWrapper.class);
            Method superPlayerExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("playerExecute", CommandWrapper.class);
            if (!playerExecuteMethod.equals(superPlayerExecuteMethod)) {
                commandUtils.playerExecute(this::playerExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method consoleExecuteMethod = this.getClass().getDeclaredMethod("consoleExecute", CommandWrapper.class);
            Method superConsoleExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("consoleExecute", CommandWrapper.class);
            if (!consoleExecuteMethod.equals(superConsoleExecuteMethod)) {
                commandUtils.consoleExecute(this::consoleExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method entityExecuteMethod = this.getClass().getDeclaredMethod("entityExecute", CommandWrapper.class);
            Method superEntityExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("entityExecute", CommandWrapper.class);
            if (!entityExecuteMethod.equals(superEntityExecuteMethod)) {
                commandUtils.entityExecute(this::entityExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method blockExecuteMethod = this.getClass().getDeclaredMethod("blockExecute", CommandWrapper.class);
            Method superBlockExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("blockExecute", CommandWrapper.class);
            if (!blockExecuteMethod.equals(superBlockExecuteMethod)) {
                commandUtils.blockExecute(this::blockExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method proxiedExecuteMethod = this.getClass().getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            Method superProxiedExecuteMethod = AbstractCommandUtils.class.getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            if (!proxiedExecuteMethod.equals(superProxiedExecuteMethod)) {
                commandUtils.proxiedExecute(this::proxiedExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        if (info() != null) commandUtils.info(info());

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

    public void register() {
        build().register(); // easier
    }
}
