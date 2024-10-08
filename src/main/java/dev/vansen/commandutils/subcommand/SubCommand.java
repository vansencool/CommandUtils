package dev.vansen.commandutils.subcommand;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.vansen.commandutils.argument.Argument;
import dev.vansen.commandutils.argument.ArgumentNester;
import dev.vansen.commandutils.command.CommandExecutor;
import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.completer.CompletionHandler;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
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
     * Adds an argument to the subcommand.
     * This method allows required arguments to be added to the command.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link Argument}
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> SubCommand argument(@NotNull Argument<T> argument) {
        RequiredArgumentBuilder<CommandSourceStack, T> arg = RequiredArgumentBuilder.argument(argument.name(), argument.type());
        argumentStack.add(arg);
        return this;
    }

    /**
     * Adds an argument to the subcommand.
     * This method allows for specifying the type of argument and its executor.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> SubCommand argument(@NotNull Argument<T> argument, @NotNull CommandExecutor executor) {
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
     * Adds an argument to the subcommand.
     * This method allows for specifying the type of argument and its executor and the completion handler.
     *
     * @param <T>      the type of the argument.
     * @param argument the {@link Argument}
     * @param executor the {@link CommandExecutor} to be executed when the argument is provided.
     * @param handler  the {@link CompletionHandler} completion handler for the argument.
     * @return this {@link SubCommand} instance for chaining.
     */
    @NotNull
    @CanIgnoreReturnValue
    public <T> SubCommand argument(@NotNull Argument<T> argument, @NotNull CommandExecutor executor, CompletionHandler handler) {
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
     * Retrieves the {@link LiteralArgumentBuilder} for this subcommand.
     *
     * @return the {@link LiteralArgumentBuilder} representing the subcommand.
     */
    @NotNull
    public LiteralArgumentBuilder<CommandSourceStack> get() {
        ArgumentNester.nest(argumentStack, builder);
        return builder;
    }
}