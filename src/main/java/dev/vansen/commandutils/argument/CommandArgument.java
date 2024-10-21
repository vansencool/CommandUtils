package dev.vansen.commandutils.argument;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.command.ExecutableSender;
import dev.vansen.commandutils.command.Position;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.messages.MessageTypes;
import dev.vansen.commandutils.permission.CommandPermission;
import dev.vansen.commandutils.sender.SenderTypes;
import dev.vansen.commandutils.subcommand.AbstractSubCommand;
import dev.vansen.commandutils.subcommand.SubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents a command argument in the command system.
 * Arguments are individual command branches with their own execution logic and other things.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class CommandArgument {
    private final RequiredArgumentBuilder<CommandSourceStack, ?> argument;
    private final List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack = new ArrayList<>();
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;
    private SenderTypes[] senderTypes = null;

    /**
     * Constructs a new command argument with the specified Argument.
     *
     * @param argument the argument.
     */
    public <T> CommandArgument(@NotNull Argument<T> argument) {
        this.argument = RequiredArgumentBuilder.argument(argument.name(), argument.type());
    }

    /**
     * Constructs a new command argument with the specified Argument and CompletionHandler.
     *
     * @param argument the argument.
     * @param handler  the completion handler.
     */
    public <T> CommandArgument(@NotNull Argument<T> argument, @NotNull CompletionHandler handler) {
        this.argument = RequiredArgumentBuilder.argument(argument.name(), argument.type());
        completion(handler);
    }

    /**
     * Constructs a new command argument with the specified name and ArgumentType.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     */
    public <T> CommandArgument(@NotNull String name, @NotNull ArgumentType<T> type) {
        this.argument = RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Constructs a new command argument with the specified name, ArgumentType, and CompletionHandler.
     *
     * @param name    the name of the argument.
     * @param type    the type of the argument.
     * @param handler the completion handler.
     */
    public <T> CommandArgument(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull CompletionHandler handler) {
        this.argument = RequiredArgumentBuilder.argument(name, type);
        completion(handler);
    }

    /**
     * Factory method to create a new instance of {@link CommandArgument}.
     *
     * @param argument the argument.
     * @return a new {@link CommandArgument} instance.
     */
    @NotNull
    public static <T> CommandArgument of(@NotNull Argument<T> argument) {
        return new CommandArgument(argument);
    }

    /**
     * Factory method to create a new instance of {@link CommandArgument} with a CompletionHandler.
     *
     * @param argument the argument.
     * @param handler  the completion handler.
     * @return a new {@link CommandArgument} instance.
     */
    @NotNull
    public static <T> CommandArgument of(@NotNull Argument<T> argument, @NotNull CompletionHandler handler) {
        return new CommandArgument(argument, handler);
    }

    /**
     * Factory method to create a new instance of {@link CommandArgument} with a name, ArgumentType, and CompletionHandler.
     *
     * @param name    the name of the argument.
     * @param type    the type of the argument.
     * @param handler the completion handler.
     * @return a new {@link CommandArgument} instance.
     */
    @NotNull
    public static <T> CommandArgument of(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull CompletionHandler handler) {
        return new CommandArgument(name, type, handler);
    }

    /**
     * Factory method to create a new instance of {@link CommandArgument} with a name and ArgumentType.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     * @return a new {@link CommandArgument} instance.
     */
    @NotNull
    public static <T> CommandArgument of(@NotNull String name, @NotNull ArgumentType<T> type) {
        return new CommandArgument(name, type);
    }

    /**
     * Creates a new string argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a string argument.
     */
    @NotNull
    public static CommandArgument string(@NotNull String name) {
        return new CommandArgument(name, StringArgumentType.string());
    }

    /**
     * Creates a new word argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a word argument.
     */
    @NotNull
    public static CommandArgument word(@NotNull String name) {
        return new CommandArgument(name, StringArgumentType.word());
    }

    /**
     * Creates a new greedy string argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a greedy string argument.
     */
    @NotNull
    public static CommandArgument greedy(@NotNull String name) {
        return new CommandArgument(name, StringArgumentType.greedyString());
    }

    /**
     * Creates a new integer argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing an integer argument.
     */
    @NotNull
    public static CommandArgument integer(@NotNull String name) {
        return new CommandArgument(name, IntegerArgumentType.integer());
    }

    /**
     * Creates a new double argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a double argument.
     */
    @NotNull
    public static CommandArgument doubleArg(@NotNull String name) {
        return new CommandArgument(name, DoubleArgumentType.doubleArg());
    }

    /**
     * Creates a new float argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a float argument.
     */
    @NotNull
    public static CommandArgument floatArg(@NotNull String name) {
        return new CommandArgument(name, FloatArgumentType.floatArg());
    }

    /**
     * Creates a new long argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a long argument.
     */
    @NotNull
    public static CommandArgument longArg(@NotNull String name) {
        return new CommandArgument(name, LongArgumentType.longArg());
    }

    /**
     * Creates a new item stack argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing an item stack argument.
     */
    @NotNull
    public static CommandArgument itemStack(@NotNull String name) {
        return new CommandArgument(name, ArgumentTypes.itemStack());
    }

    /**
     * Creates a new world argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a world argument.
     */
    @NotNull
    public static CommandArgument world(@NotNull String name) {
        return new CommandArgument(name, ArgumentTypes.world());
    }

    /**
     * Creates a new game mode argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a game mode argument.
     */
    @NotNull
    public static CommandArgument gameMode(@NotNull String name) {
        return new CommandArgument(name, ArgumentTypes.gameMode());
    }

    private void execute() {
        argument.executes(context -> {
            CommandSender sender = context.getSource().getSender();
            CommandWrapper wrapped = new CommandWrapper(context);

            try {
                switch (sender) {
                    case Player player when playerExecutor != null -> playerExecutor.execute(wrapped);
                    case ConsoleCommandSender consoleCommandSender when consoleExecutor != null ->
                            consoleExecutor.execute(wrapped);
                    case BlockCommandSender blockCommandSender when blockExecutor != null ->
                            blockExecutor.execute(wrapped);
                    default -> {
                        switch (context.getSource().getExecutor()) {
                            case null -> {
                            }
                            case Entity entity when entityExecutor != null -> entityExecutor.execute(wrapped);
                            case ProxiedCommandSender proxiedCommandSender when proxiedExecutor != null ->
                                    proxiedExecutor.execute(wrapped);
                            default -> Optional.ofNullable(defaultExecutor)
                                    .ifPresent(executor -> {
                                        if (senderTypes == null) executor.execute(wrapped);
                                        else if (Arrays.stream(senderTypes)
                                                .anyMatch(type -> type == wrapped.senderType()))
                                            executor.execute(wrapped);
                                        else {
                                            switch (wrapped.senderType()) {
                                                case PLAYER -> wrapped.response(MessageTypes.NOT_ALLOWED_PLAYER);
                                                case CONSOLE -> wrapped.response(MessageTypes.NOT_ALLOWED_CONSOLE);
                                                case ENTITY -> wrapped.response(MessageTypes.NOT_ALLOWED_ENTITY);
                                                case COMMAND_BLOCK ->
                                                        wrapped.response(MessageTypes.NOT_ALLOWED_COMMAND_BLOCK);
                                                case PROXIED ->
                                                        wrapped.response(MessageTypes.NOT_ALLOWED_PROXIED_SENDER);
                                            }
                                        }
                                    });
                        }
                    }
                }
                return 1;
            } catch (CmdException e) {
                e.send();
                return 0;
            }
        });
    }

    private void executeIf() {
        if (defaultExecutor != null || playerExecutor != null || consoleExecutor != null || blockExecutor != null || proxiedExecutor != null) {
            execute();
        }
    }

    /**
     * Sets the default executor for the argument, which is called when the argument
     * is executed without any other arguments/subcommands or when no other execution path is matched.
     *
     * @param executor the {@link CommandExecutor} to be executed by default.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument defaultExecute(@NotNull CommandExecutor executor) {
        defaultExecutor = executor;
        return this;
    }

    /**
     * Sets the default executor for the argument, which is called when the argument
     * is executed without any other arguments/subcommands or when no other execution path is matched.
     * If the command sender is not in the sender types, the executor is not called either.
     *
     * @param executor    the {@link CommandExecutor} to be executed by default.
     * @param senderTypes the {@link SenderTypes} of the sender.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument defaultExecute(@NotNull CommandExecutor executor, @NotNull ExecutableSender senderTypes) {
        defaultExecutor = executor;
        this.senderTypes = senderTypes.types();
        return this;
    }

    /**
     * Sets the executor for the argument, but only if the {@link CommandSender} is a {@link Player}.
     * If the sender is not a player, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a player.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument playerExecute(@NotNull CommandExecutor executor) {
        playerExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the argument, but only if the {@link CommandSender} is a {@link ConsoleCommandSender}.
     * If the sender is not a console, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a console.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument consoleExecute(@NotNull CommandExecutor executor) {
        consoleExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the argument, but only if the {@link CommandSender} is an {@link Entity}.
     * If the sender is not an entity, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is an entity.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument entityExecute(@NotNull CommandExecutor executor) {
        entityExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the argument, but only if the {@link CommandSender} is a {@link BlockCommandSender}.
     * If the sender is not a command block, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a block.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument blockExecute(@NotNull CommandExecutor executor) {
        blockExecutor = executor;
        return this;
    }

    /**
     * Sets the executor for the argument, but only if the {@link CommandSender} is a {@link ProxiedCommandSender}.
     * If the sender is not a proxied sender, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a proxied player.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument proxiedExecute(@NotNull CommandExecutor executor) {
        proxiedExecutor = executor;
        return this;
    }

    /**
     * Adds an argument to the argument.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link CommandArgument}
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandArgument argument(@NotNull CommandArgument argument) {
        argumentStack.add(argument.get());
        return this;
    }

    /**
     * Adds an argument to the argument.
     *
     * @param argument the {@link AbstractCommandArgument}
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull AbstractCommandArgument argument) {
        argumentStack.add(argument.build().get());
        return this;
    }

    /**
     * Adds an argument to the argument.
     * This method allows required arguments to be added to the command.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link Argument}
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandArgument argument(@NotNull Argument<T> argument) {
        RequiredArgumentBuilder<CommandSourceStack, T> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the argument.
     * This method allows for specifying the type of argument and its executor.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandArgument argument(@NotNull Argument<T> argument, @NotNull CommandExecutor executor) {
        RequiredArgumentBuilder<CommandSourceStack, T> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
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
     * Adds an argument to the argument with a completion handler.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> CommandArgument argument(@NotNull Argument<T> argument, @NotNull CommandExecutor executor, CompletionHandler handler) {
        RequiredArgumentBuilder<CommandSourceStack, T> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
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
     * Adds a completion handler to the main argument.
     *
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument completion(@NotNull CompletionHandler handler) {
        argument.suggests((context, builder) -> {
            CommandWrapper wrapped = new CommandWrapper(context);
            SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
            return handler.complete(wrapped, wrapper);
        });
        return this;
    }

    /**
     * Adds a completion handler to the first or last argument added to the argument.
     *
     * @param handler the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument completion(@NotNull Position position, @NotNull CompletionHandler handler) {
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
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument completion(int index, @NotNull CompletionHandler handler) {
        argumentStack.get(index)
                .suggests((context, builder) -> {
                    CommandWrapper wrapped = new CommandWrapper(context);
                    SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                    return handler.complete(wrapped, wrapper);
                });
        return this;
    }

    /**
     * Adds a subcommand to the argument.
     *
     * @param subCommand the {@link SubCommand} to be added.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument subCommand(@NotNull SubCommand subCommand) {
        argument.then(subCommand.get());
        return this;
    }

    /**
     * Adds a subcommand to the argument.
     *
     * @param subCommand the {@link AbstractSubCommand} to be added.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument subCommand(@NotNull AbstractSubCommand subCommand) {
        argument.then(subCommand.build().get());
        return this;
    }

    /**
     * Adds a permission to the argument.
     *
     * @param permission the {@link CommandPermission} to be added.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument permission(@NotNull CommandPermission permission) {
        if (permission.isOpPermission()) {
            argument.requires(consumer -> consumer.getSender().isOp());
        } else if (permission.getPermission() != null) {
            argument.requires(consumer -> consumer.getSender().hasPermission(permission.getPermission()));
        }
        return this;
    }

    @NotNull
    public RequiredArgumentBuilder<CommandSourceStack, ?> get() {
        ArgumentNester.nest(argument, argumentStack);
        executeIf();
        return argument;
    }
}