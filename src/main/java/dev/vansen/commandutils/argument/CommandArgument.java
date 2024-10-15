package dev.vansen.commandutils.argument;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.subcommand.SubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command argument in the command system.
 * Arguments are individual command branches with their own execution logic and other things.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class CommandArgument {
    private final RequiredArgumentBuilder<CommandSourceStack, ?> argument;
    private final List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack = new ArrayList<>();
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;

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
     * Factory method to create a new instance of {@link CommandArgument}.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     * @return a new {@link CommandArgument} instance.
     */
    @NotNull
    public static <T> CommandArgument of(@NotNull String name, @NotNull ArgumentType<T> type) {
        return new CommandArgument(name, type);
    }

    protected void execute() {
        argument.executes(context -> {
            CommandSender sender = context.getSource().getSender();
            CommandWrapper wrapped = new CommandWrapper(context);

            try {
                switch (sender) {
                    case Player player when playerExecutor != null -> playerExecutor.execute(wrapped);
                    case ConsoleCommandSender consoleCommandSender when consoleExecutor != null ->
                            consoleExecutor.execute(wrapped);
                    case Entity entity when entityExecutor != null -> entityExecutor.execute(wrapped);
                    case BlockCommandSender blockCommandSender when blockExecutor != null ->
                            blockExecutor.execute(wrapped);
                    case ProxiedCommandSender proxiedCommandSender when proxiedExecutor != null ->
                            proxiedExecutor.execute(wrapped);
                    default -> {
                        if (defaultExecutor != null) defaultExecutor.execute(wrapped);
                    }
                }
                return 1;
            } catch (CmdException e) {
                e.send();
                return 0;
            }
        });
    }

    protected void executeIf() {
        if (defaultExecutor != null || playerExecutor != null || consoleExecutor != null || blockExecutor != null || proxiedExecutor != null) {
            execute();
        }
    }

    /**
     * Sets the default executor for the argument, which is called when the argument
     * is executed without any arguments or when no other execution path is matched.
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
    public CommandArgument completion(@NotNull ArgumentPosition position, @NotNull CompletionHandler handler) {
        switch (position) {
            case ArgumentPosition.FIRST -> argumentStack.getFirst()
                    .suggests((context, builder) -> {
                        CommandWrapper wrapped = new CommandWrapper(context);
                        SuggestionsBuilderWrapper wrapper = new SuggestionsBuilderWrapper(builder);
                        return handler.complete(wrapped, wrapper);
                    });
            case ArgumentPosition.LAST -> argumentStack.getLast()
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
     * @param subcommand the {@link SubCommand} to be added.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument subCommand(@NotNull SubCommand subcommand) {
        argument.then(subcommand.get());
        return this;
    }

    @NotNull
    public RequiredArgumentBuilder<CommandSourceStack, ?> get() {
        ArgumentNester.nest(argument, argumentStack);
        executeIf();
        return argument;
    }
}