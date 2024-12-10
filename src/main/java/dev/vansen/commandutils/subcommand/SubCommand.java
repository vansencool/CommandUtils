package dev.vansen.commandutils.subcommand;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.CommandUtils;
import dev.vansen.commandutils.argument.AbstractCommandArgument;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.ArgumentNester;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.argument.finder.ArgumentString;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.*;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.messages.MessageTypes;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Represents a subcommand in a command.
 * Subcommands are individual command branches with their own execution logic and arguments.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class SubCommand {

    private final LiteralArgumentBuilder<CommandSourceStack> builder;
    private final List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack = new ArrayList<>();
    private boolean nest = true;
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor remoteConsoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;
    private SenderTypes[] senderTypes = null;

    /**
     * Constructs a new subcommand with the specified name.
     *
     * @param name the name of the subcommand.
     */
    public SubCommand(@NotNull String name) {
        this.builder = LiteralArgumentBuilder.literal(name);
    }

    /**
     * Factory method to create a new instance of {@link SubCommand}.
     *
     * @param name the name of the subcommand.
     * @return a new {@link SubCommand} instance.
     */
    @NotNull
    public static SubCommand of(@NotNull String name) {
        return new SubCommand(name);
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
     * Sets the default executor for the command, which is called when the subcommand
     * is executed without any arguments/other subcommands or when no other execution path is matched.
     *
     * @param executor the {@link CommandExecutor} to be executed by default.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand defaultExecute(@NotNull CommandExecutor executor) {
        defaultExecutor = executor;
        return this;
    }

    /**
     * Sets the default executor for the command, which is called when the subcommand
     * is executed without any arguments/other subcommands or when no other execution path is matched.
     * If the command sender is not in the sender types, the executor is not called either.
     *
     * @param executor    the {@link CommandExecutor} to be executed by default.
     * @param senderTypes the {@link SenderTypes} of the sender.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand defaultExecute(@NotNull CommandExecutor executor, @NotNull ExecutableSender senderTypes) {
        defaultExecutor = executor;
        this.senderTypes = senderTypes.types();
        return this;
    }

    /**
     * Sets the sender types for the command.
     *
     * @param types the {@link SenderTypes} of the sender.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand senderTypes(@NotNull ExecutableSender types) {
        this.senderTypes = types.types();
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link Player}.
     * If the sender is not a player, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a player.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand playerExecute(@NotNull CommandExecutor executor) {
        playerExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link ConsoleCommandSender}.
     * If the sender is not a console, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a console.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand consoleExecute(@NotNull CommandExecutor executor) {
        consoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link RemoteConsoleCommandSender}.
     * If the sender is not a remote console, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a remote console.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand remoteConsoleExecute(@NotNull CommandExecutor executor) {
        remoteConsoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is an {@link Entity}.
     * If the sender is not an entity, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is an entity.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand entityExecute(@NotNull CommandExecutor executor) {
        entityExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link BlockCommandSender}.
     * If the sender is not a command block, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a block.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand blockExecute(@NotNull CommandExecutor executor) {
        blockExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the command, but only if the {@link CommandSender} is a {@link ProxiedCommandSender}.
     * If the sender is not a proxied sender, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a proxied player.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand proxiedExecute(@NotNull CommandExecutor executor) {
        proxiedExecutor = executor;
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param argument the {@link CommandArgument}
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull CommandArgument argument) {
        argumentStack.add(argument.get());
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param argument the {@link AbstractCommandArgument}
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull AbstractCommandArgument argument) {
        argumentStack.add(argument.build().get());
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param argument the {@link Argument}
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull Argument argument, @NotNull CompletionHandler handler) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
        arg.suggests((context, builder) -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
            return handler.complete(wrapped, wrapper);
        });
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull Argument argument, @NotNull CommandExecutor executor) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
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
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull Argument argument, @NotNull CommandExecutor executor, CompletionHandler handler) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
        arg.executes(context -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            try {
                executor.execute(wrapped);
                return 1;
            } catch (CmdException e) {
                e.send();
                return 0;
            }
        }).suggests((context, builder) -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
            return handler.complete(wrapped, wrapper);
        });
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type the type of the argument.
     * @param name the name of the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull ArgumentType<?> type, @NotNull String name) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(name, type);
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type    the type of the argument.
     * @param name    the name of the argument.
     * @param handler the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull ArgumentType<?> type, @NotNull String name, @NotNull CompletionHandler handler) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(name, type);
        arg.suggests((context, builder) -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
            return handler.complete(wrapped, wrapper);
        });
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull ArgumentType<?> type, @NotNull String name, @NotNull CommandExecutor executor) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(name, type);
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
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull ArgumentType<?> type, @NotNull String name, @NotNull CommandExecutor executor, CompletionHandler handler) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(name, type);
        arg.executes(context -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            try {
                executor.execute(wrapped);
                return 1;
            } catch (CmdException e) {
                e.send();
                return 0;
            }
        }).suggests((context, builder) -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
            return handler.complete(wrapped, wrapper);
        });
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type the type of the argument.
     * @param name the name of the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull ArgumentType<?> type) {
        return argument(type, name);
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type    the type of the argument.
     * @param name    the name of the argument.
     * @param handler the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull ArgumentType<?> type, CompletionHandler handler) {
        return argument(type, name, handler);
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull CommandExecutor executor) {
        return argument(type, name, executor);
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull CommandExecutor executor, CompletionHandler handler) {
        return argument(type, name, executor, handler);
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type the type of the argument.
     * @param name the name of the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull String type) {
        return argument(type, ArgumentString.fromString(name));
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type    the type of the argument.
     * @param name    the name of the argument.
     * @param handler the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull String type, CompletionHandler handler) {
        return argument(type, ArgumentString.fromString(name), handler);
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandUtils} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull String type, @NotNull CommandExecutor executor) {
        return argument(type, ArgumentString.fromString(name), executor);
    }

    /**
     * Adds an argument to the subcommand.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand argument(@NotNull String name, @NotNull String type, @NotNull CommandExecutor executor, CompletionHandler handler) {
        return argument(type, ArgumentString.fromString(name), executor, handler);
    }

    /**
     * Adds a subcommand to the subcommand.
     * Subcommands are separate execution paths that have their own logic.
     *
     * @param subCommand the {@link SubCommand} to be added.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand subCommand(@NotNull SubCommand subCommand) {
        builder.then(subCommand.get());
        return this;
    }

    /**
     * Adds a subcommand to the subcommand.
     * Subcommands are separate execution paths that have their own logic.
     *
     * @param subCommand the {@link AbstractSubCommand} to be added.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand subCommand(@NotNull AbstractSubCommand subCommand) {
        builder.then(subCommand.build().get());
        return this;
    }

    /**
     * Disables argument nesting for the subcommand.
     * <p>
     * Note, disabling this (may) have 2 or even more arguments in a single argument, which is not recommended.
     * So it would be "/command [arg1 | arg2 | arg3]" instead of "/command arg1 arg2 arg3", there are other unexpected results as well, for example arguments not working.
     * <p>
     * Some cases where this would work would be using {@link CommandArgument} and {@link CommandArgument#argument(CommandArgument)}, but if disabled nesting in command arguments will lead to it not working.
     *
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand noNest() {
        nest = false;
        return this;
    }

    /**
     * Adds a completion handler to the last argument added to the subcommand.
     *
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand completion(@NotNull CompletionHandler handler) {
        argumentStack.getLast()
                .suggests((context, builder) -> {
                    CommandWrapper wrapped = new CommandWrapper(context);
                    SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                    return handler.complete(wrapped, wrapper);
                });
        return this;
    }

    /**
     * Adds a completion handler to the first or last argument added to the subcommand.
     *
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand completion(@NotNull Position position, @NotNull CompletionHandler handler) {
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
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand completion(int index, @NotNull CompletionHandler handler) {
        argumentStack.get(index)
                .suggests((context, builder) -> {
                    CommandWrapper wrapped = new CommandWrapper(context);
                    SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                    return handler.complete(wrapped, wrapper);
                });
        return this;
    }

    /**
     * Adds a permission requirement to the subcommand.
     *
     * @param permission the {@link CommandPermission} to be added.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand permission(@NotNull CommandPermission permission) {
        if (permission.isOpPermission()) {
            builder.requires(consumer -> consumer.getSender().isOp());
        } else if (permission.getPermission() != null) {
            builder.requires(consumer -> consumer.getSender().hasPermission(permission.getPermission()));
        }
        return this;
    }

    /**
     * The requirement of the subcommand, if the requirement is not met the subcommand will not execute, and not show in tab complete either.
     *
     * @param requirement the {@link Predicate} for the requirement
     * @return this {@link SubCommand} instance for chaining
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand requirement(@NotNull Predicate<CommandRequirement> requirement) {
        builder.requires(consumer -> requirement.test(new CommandRequirement(consumer)));
        return this;
    }

    /**
     * The requirement of the subcommand, if the requirement is not met the subcommand will not execute, and not show in tab complete either.
     *
     * @param checker the {@link BooleanChecker} for the requirement
     * @return this {@link SubCommand} instance for chaining
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand requirement(@NotNull BooleanChecker checker) {
        builder.requires(consumer -> checker.check());
        return this;
    }

    /**
     * Retrieves the {@link LiteralArgumentBuilder} for this subcommand.
     *
     * @return the {@link LiteralArgumentBuilder} representing the subcommand.
     */
    @NotNull
    public LiteralArgumentBuilder<CommandSourceStack> get() {
        executeIf();
        if (nest) ArgumentNester.nest(argumentStack, builder);
        return builder;
    }
}