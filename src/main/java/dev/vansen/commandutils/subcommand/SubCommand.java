package dev.vansen.commandutils.subcommand;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.argument.ArgumentNester;
import dev.vansen.commandutils.argument.ArgumentPosition;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
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
 * Represents a subcommand in the command system.
 * Subcommands are individual command branches with their own execution logic and arguments.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class SubCommand {

    private final LiteralArgumentBuilder<CommandSourceStack> builder;
    private final List<RequiredArgumentBuilder<CommandSourceStack, ?>> argumentStack = new ArrayList<>();
    private CommandExecutor defaultExecutor;
    private CommandExecutor playerExecutor;
    private CommandExecutor consoleExecutor;
    private CommandExecutor entityExecutor;
    private CommandExecutor blockExecutor;
    private CommandExecutor proxiedExecutor;

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

    protected void execute() {
        builder.executes(context -> {
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
     * Sets the default executor for the command, which is called when the command
     * is executed without any arguments or when no other execution path is matched.
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
     * @param <T>      the type of the argument.
     * @param argument the {@link CommandArgument}
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> SubCommand argument(@NotNull CommandArgument argument) {
        argumentStack.add(argument.get());
        return this;
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
    public SubCommand completion(@NotNull ArgumentPosition position, @NotNull CompletionHandler handler) {
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
     * Retrieves the {@link LiteralArgumentBuilder} for this subcommand.
     *
     * @return the {@link LiteralArgumentBuilder} representing the subcommand.
     */
    @NotNull
    public LiteralArgumentBuilder<CommandSourceStack> get() {
        executeIf();
        ArgumentNester.nest(argumentStack, builder);
        return builder;
    }
}