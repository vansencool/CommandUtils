package dev.vansen.commandutils.subcommand;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a subcommand in the command system.
 * Subcommands are individual command branches with their own execution logic and arguments.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class SubCommand {

    private final LiteralArgumentBuilder<CommandSourceStack> builder;
    private RequiredArgumentBuilder<CommandSourceStack, ?> currentArgument;

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

    /**
     * Sets the execution logic for the subcommand.
     * This method defines what happens when the subcommand is executed.
     *
     * @param executor the {@link CommandExecutor} that handles execution.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public SubCommand execution(@NotNull CommandExecutor executor) {
        builder.executes(context -> {
            try {
                CommandWrapper wrapped = new CommandWrapper(context);
                executor.execute(wrapped);
            } catch (CmdException e) {
                e.send();
            }
            return 1;
        });
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     * Specifies the argument's type and the executor that handles it.
     *
     * @param <T>      the type of the argument.
     * @param name     the name of the argument.
     * @param type     the {@link ArgumentType} of the argument.
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> SubCommand argument(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull CommandExecutor executor) {
        RequiredArgumentBuilder<CommandSourceStack, T> arg = RequiredArgumentBuilder.argument(name, type);
        arg.executes(context -> {
            try {
                CommandWrapper wrapped = new CommandWrapper(context);
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
     * Adds a completion handler for an argument in the subcommand.
     * Enables tab completion for the last .argument() added to the subcommand.
     *
     * @param <T>     the type of the argument.
     * @param handler the {@link CompletionHandler} that provides completion suggestions.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> SubCommand completion(@NotNull String name, @NotNull ArgumentType<T> type, @NotNull CompletionHandler handler) {
        currentArgument.suggests((context, builder) -> {
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
        return builder;
    }
}