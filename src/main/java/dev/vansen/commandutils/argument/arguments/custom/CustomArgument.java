package dev.vansen.commandutils.argument.arguments.custom;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vansen.commandutils.command.BasicCommandDetails;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Class representing an argument, which is a custom argument type.
 * This may be used to easily parse the value, or convert the native value to the target type.
 *
 * @param <T> the target type (that it will be converted to, or parsed to)
 * @param <N> the native type (the type that is used in the command)
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public abstract class CustomArgument<T, N> implements CustomArgumentType.Converted<T, N> {
    private final ArgumentType<N> nativeType;

    /**
     * Creates a new custom argument with the specified native type.
     *
     * @param nativeType the native type of the argument
     */
    public CustomArgument(@NotNull ArgumentType<N> nativeType) {
        this.nativeType = nativeType;
    }

    @Override
    public @NotNull ArgumentType<N> getNativeType() {
        return nativeType;
    }

    @Override
    public @NotNull T convert(@NotNull N nativeValue) throws CmdSyntaxException {
        return parseOrConvert(nativeValue);
    }

    /**
     * Parses or converts the native value to the target type.
     * This may be used to validate the native value, or to convert it to the target type.
     *
     * @param nativeValue the native value to parse or convert
     * @return the parsed or converted value
     * @throws CmdSyntaxException if the native value is invalid to parse or convert
     */
    public abstract @NotNull T parseOrConvert(@NotNull N nativeValue) throws CmdSyntaxException;

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof CommandSourceStack stack)) {
            LoggerFactory.getLogger("CommandUtils").error("The command source is not a CommandSourceStack. This should not have happened.");
            return builder.buildFuture();
        }
        return suggest(new BasicCommandDetails(stack), new SuggestionsBuilderWrapper(builder));
    }

    /**
     * Provides suggestions for the argument based on the command context and the suggestions builder.
     *
     * @param context the command context containing information about the command execution
     * @param wrapper the suggestions builder wrapper to build suggestions
     * @return a CompletableFuture that will complete with the generated suggestions
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggest(@NotNull BasicCommandDetails context, @NotNull SuggestionsBuilderWrapper wrapper);
}