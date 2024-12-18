package dev.vansen.commandutils.argument;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.argument.arguments.ColorArgumentType;
import dev.vansen.commandutils.argument.arguments.CommandBlockModeArgumentType;
import dev.vansen.commandutils.argument.arguments.PlayerArgumentType;
import dev.vansen.commandutils.argument.finder.ArgumentString;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.*;
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
 * Represents a command argument in the command system.
 * Arguments are individual command branches with their own execution logic and other things.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class CommandArgument {
    private final RequiredArgumentBuilder<CommandSourceStack, ?> argument;
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
     * Constructs a new command argument with the specified Argument.
     *
     * @param argument the argument.
     */
    public CommandArgument(@NotNull Argument argument) {
        this.argument = RequiredArgumentBuilder.argument(argument.name(), argument.type());
    }

    /**
     * Constructs a new command argument with the specified Argument and CompletionHandler.
     *
     * @param argument the argument.
     * @param handler  the completion handler.
     */
    public CommandArgument(@NotNull Argument argument, @NotNull CompletionHandler handler) {
        this.argument = RequiredArgumentBuilder.argument(argument.name(), argument.type());
        completion(handler);
    }

    /**
     * Constructs a new command argument with the specified name and ArgumentType.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     */
    public CommandArgument(@NotNull String name, @NotNull ArgumentType<?> type) {
        this.argument = RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Constructs a new command argument with the specified name, ArgumentType, and CompletionHandler.
     *
     * @param name    the name of the argument.
     * @param type    the type of the argument.
     * @param handler the completion handler.
     */
    public CommandArgument(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull CompletionHandler handler) {
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
    public static CommandArgument of(@NotNull Argument argument) {
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
    public static CommandArgument of(@NotNull Argument argument, @NotNull CompletionHandler handler) {
        return new CommandArgument(argument, handler);
    }

    /**
     * Factory method to create a new instance of {@link CommandArgument} with a name and ArgumentType.
     *
     * @param name the name of the argument.
     * @param type the type of the argument.
     * @return a new {@link CommandArgument} instance.
     */
    @NotNull
    public static CommandArgument of(@NotNull String name, @NotNull ArgumentType<?> type) {
        return new CommandArgument(name, type);
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
    public static CommandArgument of(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull CompletionHandler handler) {
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
    public static CommandArgument of(@NotNull String name, @NotNull String type) {
        return new CommandArgument(name, ArgumentString.fromString(type));
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
    public static CommandArgument of(@NotNull String name, @NotNull String type, @NotNull CompletionHandler handler) {
        return new CommandArgument(name, ArgumentString.fromString(type), handler);
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
     * Creates a new boolean argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a boolean argument.
     */
    @NotNull
    public static CommandArgument bool(@NotNull String name) {
        return new CommandArgument(name, BoolArgumentType.bool());
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
     * Creates a new integer argument with the specified name and minimum value.
     *
     * @param name the name of the argument.
     * @param min  the minimum value for the integer argument.
     * @return a new {@link CommandArgument} instance representing an integer argument.
     */
    @NotNull
    public static CommandArgument integer(@NotNull String name, int min) {
        return new CommandArgument(name, IntegerArgumentType.integer(min));
    }

    /**
     * Creates a new integer argument with the specified name and a minimum and maximum value.
     *
     * @param name the name of the argument.
     * @param max  the maximum value for the integer argument.
     * @return a new {@link CommandArgument} instance representing an integer argument.
     */
    @NotNull
    public static CommandArgument integer(@NotNull String name, int min, int max) {
        return new CommandArgument(name, IntegerArgumentType.integer(min, max));
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
     * Creates a new player argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a player argument.
     */
    @NotNull
    public static CommandArgument player(@NotNull String name) {
        return new CommandArgument(name, PlayerArgumentType.player());
    }

    /**
     * Creates a new color argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a color argument.
     */
    @NotNull
    public static CommandArgument color(@NotNull String name) {
        return new CommandArgument(name, ColorArgumentType.color());
    }

    /**
     * Creates a new named color argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a named color argument.
     */
    @NotNull
    public static CommandArgument namedColor(@NotNull String name) {
        return new CommandArgument(name, ArgumentTypes.namedColor());
    }

    /**
     * Creates a new block mode argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a block mode argument.
     */
    @NotNull
    public static CommandArgument blockMode(@NotNull String name) {
        return new CommandArgument(name, CommandBlockModeArgumentType.mode());
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

    /**
     * Creates a new uuid argument with the specified name.
     *
     * @param name the name of the argument.
     * @return a new {@link CommandArgument} instance representing a uuid argument.
     */
    @NotNull
    public static CommandArgument uuid(@NotNull String name) {
        return new CommandArgument(name, ArgumentTypes.uuid());
    }

    private void execute() {
        argument.executes(context -> {
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
     * Sets the sender types for the argument.
     *
     * @param senderTypes the {@link SenderTypes} of the sender.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument senderTypes(@NotNull ExecutableSender senderTypes) {
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
     * Sets the executor for the argument, but only if the {@link CommandSender} is a {@link RemoteConsoleCommandSender}.
     * If the sender is not a remote console, the executor is not called.
     *
     * @param executor the {@link CommandExecutor} to be executed if the sender is a remote console.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument remoteConsoleExecute(@NotNull CommandExecutor executor) {
        remoteConsoleExecutor = executor;
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
     * @param argument the {@link CommandArgument}
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull CommandArgument argument) {
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
     *
     * @param argument the {@link Argument}
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull Argument argument, @NotNull CompletionHandler handler) {
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
     * Adds an argument to the argument.
     *
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull Argument argument, @NotNull CommandExecutor executor) {
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
     * Adds an argument to the argument.
     *
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull Argument argument, @NotNull CommandExecutor executor, CompletionHandler handler) {
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
     * Adds an argument to the argument.
     *
     * @param type the type of the argument.
     * @param name the name of the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull ArgumentType<?> type, @NotNull String name) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = RequiredArgumentBuilder.argument(name, type);
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type    the type of the argument.
     * @param name    the name of the argument.
     * @param handler the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull ArgumentType<?> type, @NotNull String name, @NotNull CompletionHandler handler) {
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
     * Adds an argument to the argument.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull ArgumentType<?> type, @NotNull String name, @NotNull CommandExecutor executor) {
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
     * Adds an argument to the argument.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull ArgumentType<?> type, @NotNull String name, @NotNull CommandExecutor executor, CompletionHandler handler) {
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
     * Adds an argument to the argument.
     *
     * @param type the type of the argument.
     * @param name the name of the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull ArgumentType<?> type) {
        return argument(type, name);
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type    the type of the argument.
     * @param name    the name of the argument.
     * @param handler the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull ArgumentType<?> type, CompletionHandler handler) {
        return argument(type, name, handler);
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull CommandExecutor executor) {
        return argument(type, name, executor);
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull ArgumentType<?> type, @NotNull CommandExecutor executor, CompletionHandler handler) {
        return argument(type, name, executor, handler);
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type the type of the argument.
     * @param name the name of the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull String type) {
        return argument(type, ArgumentString.fromString(name));
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type    the type of the argument.
     * @param name    the name of the argument.
     * @param handler the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull String type, CompletionHandler handler) {
        return argument(type, ArgumentString.fromString(name), handler);
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull String type, @NotNull CommandExecutor executor) {
        return argument(type, ArgumentString.fromString(name), executor);
    }

    /**
     * Adds an argument to the argument.
     *
     * @param type     the type of the argument.
     * @param name     the name of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} for the argument.
     * @return this {@link CommandArgument} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument argument(@NotNull String name, @NotNull String type, @NotNull CommandExecutor executor, CompletionHandler handler) {
        return argument(type, ArgumentString.fromString(name), executor, handler);
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
     * @param position the position of the argument to add the completion handler to.
     * @param handler  the {@link CompletionHandler} completion handler for the argument.
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
     * Disables argument nesting for the argument.
     * <p>
     * Note, disabling this (may) have 2 or even more arguments in a single argument, which is not recommended.
     * So it would be "/command [arg1 | arg2 | arg3]" instead of "/command arg1 arg2 arg3", there are other unexpected results as well, for example arguments not working.
     * <p>
     * Some cases where this would work would be using {@link CommandArgument} and {@link CommandArgument#argument(CommandArgument)}, but if disabled nesting in command arguments will lead to it not working.
     *
     * @return this {@link CommandArgument} instance for chaining.
     */
    public CommandArgument noNest() {
        nest = false;
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

    /**
     * The requirement of the subcommand, if the requirement is not met the argument will not execute.
     *
     * @param requirement the {@link Predicate} for the requirement
     * @return this {@link CommandArgument} instance for chaining
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument requirement(@NotNull Predicate<CommandRequirement> requirement) {
        argument.requires(consumer -> requirement.test(new CommandRequirement(consumer)));
        return this;
    }

    /**
     * The requirement of the argument, if the requirement is not met the argument will not execute.
     *
     * @param checker the {@link BooleanChecker} for the requirement
     * @return this {@link CommandArgument} instance for chaining
     */
    @NotNull
    @CanIgnoreReturnValue
    public CommandArgument requirement(@NotNull BooleanChecker checker) {
        argument.requires(consumer -> checker.check());
        return this;
    }

    @NotNull
    public RequiredArgumentBuilder<CommandSourceStack, ?> get() {
        executeIf();
        if (nest) ArgumentNester.nest(argument, argumentStack);
        return argument;
    }
}