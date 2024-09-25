package dev.vansen.commandutils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.api.CommandAPI;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.subcommand.SubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class for building and registering Minecraft commands using Brigadier.
 * This class allows for defining permissions, arguments, completions, subcommands,
 * and more in a fluent and customizable way.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class CommandUtils {

    private final LiteralArgumentBuilder<CommandSourceStack> builder;
    private RequiredArgumentBuilder<CommandSourceStack, ?> currentArgument;
    @Nullable
    private String description = null;
    @Nullable
    private List<String> aliases = null;


    // Private constructor
    private CommandUtils(@NotNull String commandName) {
        builder = LiteralArgumentBuilder.literal(commandName);
    }

    /**
     * Factory method to create a new command builder.
     *
     * @param commandName the name of the command.
     * @return a new instance of {@link CommandUtils}.
     */
    @NotNull
    public static CommandUtils newCommand(@NotNull String commandName) {
        return new CommandUtils(commandName);
    }

    /**
     * Sets the default executor for the command, which is called when the command
     * is executed without any arguments or when no other execution path is matched.
     *
     * @param executor the {@link CommandExecutor} to be executed by default.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils defaultExecute(@NotNull CommandExecutor executor) {
        try {
            builder.executes(context -> {
                CommandWrapper wrapped = new CommandWrapper(context);
                executor.execute(wrapped);
                return 1;
            });
        } catch (CmdException e) {
            e.send();
        }
        return this;
    }

    /**
     * Adds an argument to the command.
     * This method allows for specifying the type of argument and its executor.
     *
     * @param <T>      the type of the argument.
     * @param name     the name of the argument.
     * @param type     the {@link ArgumentType} of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandUtils argument(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull CommandExecutor executor) {
        RequiredArgumentBuilder<CommandSourceStack, T> arg = RequiredArgumentBuilder.argument(name, type);
        arg.executes(context -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            try {
                executor.execute(wrapped);
                return 1;
            } catch (CmdException e) {
                e.send();
                return 0;
            }
        });
        builder.then(arg);
        currentArgument = arg;
        return this;
    }

    /**
     * Adds a completion handler for an argument.
     * This enables tab-completion support for the argument based on the current .argument() call.
     *
     * @param <T>     the type of the argument.
     * @param handler the {@link CompletionHandler} responsible for providing suggestions.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandUtils completion(@NotNull CompletionHandler handler) {
        currentArgument.suggests((context, builder) -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
            return handler.complete(wrapped, wrapper);
        });
        return this;
    }

    /**
     * Adds a subcommand to the main command.
     * Subcommands are separate execution paths that have their own logic.
     *
     * @param subCommand the {@link SubCommand} to be added.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils subCommand(@NotNull SubCommand subCommand) {
        builder.then(subCommand.get());
        return this;
    }

    /**
     * Adds additional metadata to the command, such as aliases, description, and permissions.
     *
     * @param info a {@link Consumer} that accepts a {@link CommandInfo} object for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils info(@NotNull Consumer<CommandInfo> info) {
        CommandInfo commandInfo = CommandInfo.info();
        info.accept(commandInfo);
        this.aliases = commandInfo.getAliases();
        this.description = commandInfo.getDescription();
        CommandPermission permission = commandInfo.getPermission();
        if (permission == null) return this;
        if (permission.isOpPermission()) {
            builder.requires(consumer -> consumer.getSender().isOp());
        } else if (permission.getPermission() != null) {
            builder.requires(consumer -> consumer.getSender().hasPermission(permission.getPermission()));
        }
        return this;
    }

    /**
     * Registers the command with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build() {
        CommandAPI.get().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(builder.build(), description, aliases == null ? List.of() : aliases);
        });
    }

    /**
     * Registers the command under that plugin's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build(@NotNull LifecycleEventManager<Plugin> plugin) {
        plugin.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(builder.build(), description, aliases == null ? List.of() : aliases);
        });
    }

    /**
     * Registers the command under that plugin's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build(@NotNull JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(builder.build(), description, aliases == null ? List.of() : aliases);
        });
    }

    /**
     * Registers the command under that pluginmeta's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build(@NotNull PluginMeta meta) {
        CommandAPI.get().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(meta, builder.build(), description, aliases == null ? List.of() : aliases);
        });
    }

    /**
     * @param namespace the namespace of the command.
     * @see #build()
     * @see #build(JavaPlugin)
     * @see #build(LifecycleEventManager)
     * @deprecated This method is not recommended for use because it does not support command descriptions and usages properly.
     * <p></p>
     * Additionally, commands registered using this method will be displayed as "A Mojang provided command" in the `/help` output.
     */
    @Deprecated
    public void build(@NotNull String namespace) {
        CommandAPI.get().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.getDispatcher().register(builder);
            commands.getDispatcher().register(LiteralArgumentBuilder
                    .<CommandSourceStack>literal(namespace + ":" + builder.getLiteral())
                    .redirect(builder.build()));
            if (aliases != null && !aliases.isEmpty()) {
                for (String alias : aliases) {
                    commands.getDispatcher().register(LiteralArgumentBuilder
                            .<CommandSourceStack>literal(alias)
                            .redirect(builder.build()));
                    commands.getDispatcher().register(LiteralArgumentBuilder
                            .<CommandSourceStack>literal(namespace + ":" + alias)
                            .redirect(builder.build()));
                }
            }
        });
    }
}