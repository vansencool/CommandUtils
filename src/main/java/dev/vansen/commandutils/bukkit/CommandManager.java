package dev.vansen.commandutils.bukkit;

import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.bukkit.argument.ArgumentParser;
import dev.vansen.commandutils.bukkit.argument.BukkitArgument;
import dev.vansen.commandutils.bukkit.command.BukkitCommands;
import dev.vansen.commandutils.bukkit.tabcompletion.AsyncTabCompleter;
import dev.vansen.commandutils.bukkit.tabcompletion.TabCompleter;
import dev.vansen.commandutils.info.CommandInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Class for registering the bukkit (not actual bukkit) commands.
 */
@SuppressWarnings("unused")
public class CommandManager {

    /**
     * Registers a Bukkit command with the specified info.
     *
     * @param command the command to register
     * @param info    the command info
     */
    public static void register(@NotNull BukkitCommands command, @NotNull BukkitInfo info) {
        CommandUtils registrar = CommandUtils.command(info.name())
                .info(CommandInfo.info()
                        .description(info.description())
                        .aliases(info.aliases())
                        .permission(info.permission()));
        if (info.args().isEmpty() || new ArgumentParser(info.args()).length() == 0) {
            registrar.defaultExecute(context -> command.execute(new BukkitArgument(context, info.args())));
            if (new ArgumentParser(info.args()).hasArgs()) {
                CommandArgument argument = CommandArgument.greedy("args");
                registrar.argument(argument.defaultExecute(context -> command.execute(new BukkitArgument(context, info.args()))));
                complete(argument, command, info);
            }
        } else {
            CommandArgument argument = CommandArgument.greedy("args");
            registrar.defaultExecute(context -> {
                if (context.inputWithoutCommand().isEmpty() || new ArgumentParser(info.args())
                        .length() > context.inputWithoutCommand().trim().split(" ").length) {
                    context.response("<red>Not enough arguments! Usage: /" + info.name() + " " + info.args());
                }
            });
            registrar.argument(CommandArgument.greedy("args")
                    .defaultExecute(context -> {
                        if (context.inputWithoutCommand().isEmpty() || new ArgumentParser(info.args())
                                .length() > context.inputWithoutCommand().split(" ").length) {
                            context.response("<red>Not enough arguments! Usage: /" + info.name() + " " + info.args());
                            return;
                        }
                        command.execute(new BukkitArgument(context, info.args()));
                    }));
            complete(argument, command, info);
        }
        registrar.register();
    }

    /**
     * Registers a Bukkit command with the specified name.
     *
     * @param command the command to register
     * @param name    the command name
     */
    public static void register(@NotNull BukkitCommands command, @NotNull String name) {
        register(command, BukkitInfo.info()
                .name(name));
    }

    /**
     * Registers a Bukkit command with the specified name and args.
     *
     * @param command the command to register
     * @param name    the command name
     * @param args    the command args
     */
    public static void register(@NotNull BukkitCommands command, @NotNull String name, @NotNull String args) {
        register(command, BukkitInfo.info()
                .name(name)
                .args(args));
    }

    private static void complete(@NotNull CommandArgument argument, @NotNull BukkitCommands command, @NotNull BukkitInfo info) {
        argument.completion((context, wrapper) -> {
            if (command instanceof TabCompleter completer) {
                List<String> suggestions = completer.tabComplete(new BukkitArgument(context, info.args()), wrapper);
                if (suggestions == null) return wrapper.build();
                suggestions.stream()
                        .filter(string -> string.toLowerCase().startsWith(wrapper.currentArgLowercase()))
                        .forEach(wrapper::suggest);
                return wrapper.build();
            } else if (command instanceof AsyncTabCompleter completer) {
                CompletableFuture<List<String>> suggestions = completer.tabComplete(new BukkitArgument(context, info.args()), wrapper);
                if (suggestions == null) return wrapper.build();
                return suggestions.thenComposeAsync(strings -> {
                    strings.stream()
                            .filter(string -> string.toLowerCase().startsWith(wrapper.currentArgLowercase()))
                            .forEach(wrapper::suggest);
                    return wrapper.build();
                });
            } else return wrapper.build();
        });
    }
}