package dev.vansen.commandutils;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.api.CommandAPI;
import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.ArgumentNester;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.info.Aliases;
import dev.vansen.commandutils.info.CommandInfo;
import dev.vansen.commandutils.messages.MessageTypes;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for building and registering Minecraft commands using Brigadier.
 * This class allows for defining permissions, arguments, completions, subcommands,
 * and more in a fluent and customizable way.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class CommandUtils {

    private final LiteralArgumentBuilder<CommandSourceStack> builder;
    private final List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack = new ArrayList<>();
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor remoteConsoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;
    private SenderTypes[] senderTypes = null;
    @Nullable
    private String description = null;
    @Nullable
    private List<String> aliases = null;

    /**
     * Constructs a new command builder with the specified name.
     *
     * @param commandName the name of the command.
     */
    public CommandUtils(@NotNull String commandName) {
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
     * Factory method to create a new command builder.
     *
     * @param commandName the name of the command.
     * @return a new instance of {@link CommandUtils}.
     */
    @NotNull
    public static CommandUtils command(@NotNull String commandName) {
        return new CommandUtils(commandName);
    }

    private void execute() {
        builder.executes(context -> {
            CommandSender sender = context.getSource().getSender();
            CommandWrapper wrapped = new CommandWrapper(context);
            boolean done = false;

            try {
                switch (sender) {
                    case Player player when playerExecutor != null -> {
                        done = true;
                        playerExecutor.execute(wrapped);
                    }
                    case ConsoleCommandSender consoleCommandSender when consoleExecutor != null -> {
                        done = true;
                        consoleExecutor.execute(wrapped);
                    }
                    case RemoteConsoleCommandSender remoteConsoleCommandSender when remoteConsoleExecutor != null -> {
                        done = true;
                        remoteConsoleExecutor.execute(wrapped);
                    }
                    case BlockCommandSender blockCommandSender when blockExecutor != null -> {
                        done = true;
                        blockExecutor.execute(wrapped);
                    }
                    default -> {
                        switch (context.getSource().getExecutor()) {
                            case Entity entity when entityExecutor != null -> {
                                done = true;
                                entityExecutor.execute(wrapped);
                            }
                            case ProxiedCommandSender proxiedCommandSender when proxiedExecutor != null -> {
                                done = true;
                                proxiedExecutor.execute(wrapped);
                            }
                            case null, default -> {
                            }
                        }
                    }
                }
                if (!done) {
                    Optional.ofNullable(defaultExecutor)
                            .ifPresent(executor -> {
                                if (senderTypes == null) executor.execute(wrapped);
                                else if (Arrays.stream(senderTypes)
                                        .anyMatch(type -> type == wrapped.senderType()))
                                    executor.execute(wrapped);
                                else {
                                    switch (wrapped.senderType()) {
                                        case PLAYER -> wrapped.response(MessageTypes.NOT_ALLOWED_PLAYER);
                                        case CONSOLE -> wrapped.response(MessageTypes.NOT_ALLOWED_CONSOLE);
                                        case REMOTE_CONSOLE ->
                                                wrapped.response(MessageTypes.NOT_ALLOWED_REMOTE_CONSOLE);
                                        case ENTITY -> wrapped.response(MessageTypes.NOT_ALLOWED_ENTITY);
                                        case COMMAND_BLOCK -> wrapped.response(MessageTypes.NOT_ALLOWED_COMMAND_BLOCK);
                                        case PROXIED -> wrapped.response(MessageTypes.NOT_ALLOWED_PROXIED_SENDER);
                                    }
                                }
                            });
                }
                return 1;
            } catch (CmdException e) {
                e.send();
                return 0;
            }
        });
    }

    private void executeIf() {
        if (defaultExecutor != null || playerExecutor != null || consoleExecutor != null || remoteConsoleExecutor != null || entityExecutor != null || blockExecutor != null || proxiedExecutor != null) {
            execute();
        }
    }

    /**
     * Sets the default executor for the command, which is called when the command
     * is executed without any arguments/subcommands or when no other execution path is matched.
     *
     * @param executor the {@link CommandExecutor} to be executed by default.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils defaultExecute(@NotNull CommandExecutor executor) {
        defaultExecutor = executor;
        return this;
    }

    /**
     * Sets the default executor for the command, which is called when the command
     * is executed without any arguments/subcommands or when no other execution path is matched.
     * If the command sender is not in the sender types, the executor is not called either.
     *
     * @param executor    the {@link CommandExecutor} to be executed by default.
     * @param senderTypes the {@link SenderTypes} of the sender.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils defaultExecute(@NotNull CommandExecutor executor, @NotNull ExecutableSender senderTypes) {
        defaultExecutor = executor;
        this.senderTypes = senderTypes.types();
        return this;
    }

    /**
     * Sets the sender types for the command.
     *
     * @param senderTypes the {@link SenderTypes} of the sender.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils senderTypes(@NotNull ExecutableSender senderTypes) {
        this.senderTypes = senderTypes.types();
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link Player}.
     * If the sender is not a player, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a player.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils playerExecute(@NotNull CommandExecutor executor) {
        playerExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link ConsoleCommandSender}.
     * If the sender is not a console, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a console.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils consoleExecute(@NotNull CommandExecutor executor) {
        consoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link RemoteConsoleCommandSender}.
     * If the sender is not a remote console, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a remote console.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils remoteConsoleExecute(@NotNull CommandExecutor executor) {
        remoteConsoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is an {@link Entity}.
     * If the sender is not an entity, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is an entity.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils entityExecute(@NotNull CommandExecutor executor) {
        entityExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link BlockCommandSender}.
     * If the sender is not a command block, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a block.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils blockExecute(@NotNull CommandExecutor executor) {
        blockExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link ProxiedCommandSender}.
     * If the sender is not a proxied sender, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a proxied player.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils proxiedExecute(@NotNull CommandExecutor executor) {
        proxiedExecutor = executor;
        return this;
    }

    /**
     * Adds an argument to the command.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link CommandArgument}
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandUtils argument(@NotNull CommandArgument argument) {
        argumentStack.add(argument.get());
        return this;
    }

    /**
     * Adds an argument to the command.
     *
     * @param argument the {@link AbstractCommandArgument}
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils argument(@NotNull AbstractCommandArgument argument) {
        argumentStack.add(argument.build().get());
        return this;
    }

    /**
     * Adds a completion handler to the last argument added to the command.
     *
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils completion(@NotNull CompletionHandler handler) {
        argumentStack.getLast()
                .suggests((context, builder) -> {
                    CommandWrapper wrapped = new CommandWrapper(context);
                    SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                    return handler.complete(wrapped, wrapper);
                });
        return this;
    }

    /**
     * Adds a completion handler to the first or last argument added to the command.
     *
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils completion(@NotNull Position position, @NotNull CompletionHandler handler) {
        switch (position) {
            case Position.FIRST -> argumentStack.getFirst()
                    .suggests((context, builder) -> {
                        CommandWrapper wrapped = new CommandWrapper(context);
                        SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                        return handler.complete(wrapped, wrapper);
                    });
            case Position.LAST -> argumentStack.getLast()
                    .suggests((context, builder) -> {
                        CommandWrapper wrapped = new CommandWrapper(context);
                        SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                        return handler.complete(wrapped, wrapper);
                    });
        }
        return this;
    }

    /**
     * Adds a completion handler to the argument at the specified index.
     *
     * @param index   the index of the argument to add the completion handler to.
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils completion(int index, @NotNull CompletionHandler handler) {
        argumentStack.get(index)
                .suggests((context, builder) -> {
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
     * Adds a subcommand to the main command.
     * Subcommands are separate execution paths that have their own logic.
     *
     * @param subCommand the {@link AbstractSubCommand} to be added.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils subCommand(@NotNull AbstractSubCommand subCommand) {
        builder.then(subCommand.build().get());
        return this;
    }

    /**
     * Adds additional metadata to the command, such as aliases, description, and permissions.
     *
     * @param info a {@link CommandInfo} for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils info(@NotNull CommandInfo info) {
        this.aliases = info.getAliases();
        this.description = info.getDescription();
        CommandPermission permission = info.getPermission();
        if (permission == null) return this;
        if (permission.isOpPermission()) {
            builder.requires(consumer -> consumer.getSender().isOp());
        } else if (permission.getPermission() != null) {
            builder.requires(consumer -> consumer.getSender().hasPermission(permission.getPermission()));
        }
        return this;
    }

    /**
     * Sets the aliases of the command using an {@link Aliases} instance.
     *
     * @param aliases a {@link Aliases} for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils aliases(@NotNull Aliases aliases) {
        this.aliases = aliases.getAliases();
        return this;
    }

    /**
     * Sets the aliases of the command using an array of strings.
     *
     * @param aliases an array of strings for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils aliases(@NotNull String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    /**
     * Sets the aliases of the command using a list of strings.
     *
     * @param aliases a list of strings for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils aliases(@NotNull List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    /**
     * Sets the permission of the command using a {@link CommandPermission} instance.
     *
     * @param permission a {@link CommandPermission} for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils permission(@NotNull CommandPermission permission) {
        if (permission.isOpPermission()) {
            builder.requires(consumer -> consumer.getSender().isOp());
        } else if (permission.getPermission() != null) {
            builder.requires(consumer -> consumer.getSender().hasPermission(permission.getPermission()));
        }
        return this;
    }

    /**
     * Sets the permission of the command using a string permission.
     *
     * @param permission a string permission for setting metadata.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandUtils permission(@NotNull String permission) {
        builder.requires(consumer -> consumer.getSender().hasPermission(permission));
        return this;
    }

    /**
     * Registers the command with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build() {
        CommandAPI.get().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            executeIf();
            ArgumentNester.nest(argumentStack, builder);
            commands.register(builder.build(), description, aliases == null ? List.of() : aliases);
        });
    }

    /**
     * Registers the command under that plugin's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build(@NotNull LifecycleEventManager<@NotNull Plugin> plugin) {
        plugin.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            executeIf();
            ArgumentNester.nest(argumentStack, builder);
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
            executeIf();
            ArgumentNester.nest(argumentStack, builder);
            commands.register(builder.build(), description, aliases == null ? List.of() : aliases);
        });
    }

    /**
     * Registers the command under that plugin meta's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void build(@NotNull PluginMeta meta) {
        CommandAPI.get().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            executeIf();
            ArgumentNester.nest(argumentStack, builder);
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
            executeIf();
            ArgumentNester.nest(argumentStack, builder);
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

    /**
     * Registers the command with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void register() {
        build();
    }

    /**
     * Registers the command under that plugin's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void register(@NotNull LifecycleEventManager<@NotNull Plugin> plugin) {
        build(plugin);
    }

    /**
     * Registers the command under that plugin's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void register(@NotNull JavaPlugin plugin) {
        build(plugin);
    }

    /**
     * Registers the command under that plugin meta's name with the provided description and aliases.
     * This method should be used in most cases as it handles both namespaces and command registration fully.
     */
    public void register(@NotNull PluginMeta meta) {
        build(meta);
    }

    /**
     * @param namespace the namespace of the command.
     * @see #build()
     * @see #build(PluginMeta)
     * @see #build(JavaPlugin)
     * @see #build(LifecycleEventManager)
     * @deprecated This method is not recommended for use because it does not support command descriptions and usages properly.
     * <p></p>
     * Additionally, commands registered using this method will be displayed as "A Mojang provided command" in the `/help` output.
     */
    @Deprecated
    public void register(@NotNull String namespace) {
        build(namespace);
    }
}