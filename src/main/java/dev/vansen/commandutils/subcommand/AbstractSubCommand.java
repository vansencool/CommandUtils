package dev.vansen.commandutils.subcommand;

import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.permission.CommandPermission;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractSubCommand {

    private final String name;

    public AbstractSubCommand(@NotNull String name) {
        this.name = name;
    }

    public void execute(@NotNull CommandWrapper context) {
        // Default implementation
    }

    public void playerExecute(@NotNull CommandWrapper context) {
        // Default implementation
    }

    public void consoleExecute(@NotNull CommandWrapper context) {
        // Default implementation
    }

    public void entityExecute(@NotNull CommandWrapper context) {
        // Default implementation
    }

    public void blockExecute(@NotNull CommandWrapper context) {
        // Default implementation
    }

    public void proxiedExecute(@NotNull CommandWrapper context) {
        // Default implementation
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

    public SubCommand build() {
        SubCommand sub = SubCommand.of(name);

        try {
            Method executeMethod = this.getClass().getDeclaredMethod("execute", CommandWrapper.class);
            Method superExecuteMethod = AbstractSubCommand.class.getDeclaredMethod("execute", CommandWrapper.class);
            if (!executeMethod.equals(superExecuteMethod)) {
                sub.defaultExecute(this::execute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method playerExecuteMethod = this.getClass().getDeclaredMethod("playerExecute", CommandWrapper.class);
            Method superPlayerExecuteMethod = AbstractSubCommand.class.getDeclaredMethod("playerExecute", CommandWrapper.class);
            if (!playerExecuteMethod.equals(superPlayerExecuteMethod)) {
                sub.playerExecute(this::playerExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method consoleExecuteMethod = this.getClass().getDeclaredMethod("consoleExecute", CommandWrapper.class);
            Method superConsoleExecuteMethod = AbstractSubCommand.class.getDeclaredMethod("consoleExecute", CommandWrapper.class);
            if (!consoleExecuteMethod.equals(superConsoleExecuteMethod)) {
                sub.consoleExecute(this::consoleExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method entityExecuteMethod = this.getClass().getDeclaredMethod("entityExecute", CommandWrapper.class);
            Method superEntityExecuteMethod = AbstractSubCommand.class.getDeclaredMethod("entityExecute", CommandWrapper.class);
            if (!entityExecuteMethod.equals(superEntityExecuteMethod)) {
                sub.entityExecute(this::entityExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method blockExecuteMethod = this.getClass().getDeclaredMethod("blockExecute", CommandWrapper.class);
            Method superBlockExecuteMethod = AbstractSubCommand.class.getDeclaredMethod("blockExecute", CommandWrapper.class);
            if (!blockExecuteMethod.equals(superBlockExecuteMethod)) {
                sub.blockExecute(this::blockExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method proxiedExecuteMethod = this.getClass().getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            Method superProxiedExecuteMethod = AbstractSubCommand.class.getDeclaredMethod("proxiedExecute", CommandWrapper.class);
            if (!proxiedExecuteMethod.equals(superProxiedExecuteMethod)) {
                sub.proxiedExecute(this::proxiedExecute);
            }
        } catch (NoSuchMethodException ignored) {
        }

        if (permission() != null) sub.permission(permission());

        if (argumentPosition() == Position.LAST) {
            if (abstractSubCommandPosition() == Position.LAST) {
                subCommands().forEach(sub::subCommand);
                abstractSubCommands().forEach(sub::subCommand);
            } else {
                abstractSubCommands().forEach(sub::subCommand);
                subCommands().forEach(sub::subCommand);
            }
            if (abstractArgumentPosition() == Position.LAST) {
                arguments().forEach(sub::argument);
                abstractArguments().forEach(sub::argument);
            } else {
                abstractArguments().forEach(sub::argument);
                arguments().forEach(sub::argument);
            }
        } else {
            if (abstractArgumentPosition() == Position.LAST) {
                arguments().forEach(sub::argument);
                abstractArguments().forEach(sub::argument);
            } else {
                abstractArguments().forEach(sub::argument);
                arguments().forEach(sub::argument);
            }
            if (abstractSubCommandPosition() == Position.LAST) {
                subCommands().forEach(sub::subCommand);
                abstractSubCommands().forEach(sub::subCommand);
            } else {
                abstractSubCommands().forEach(sub::subCommand);
                subCommands().forEach(sub::subCommand);
            }
        }

        return sub;
    }
}