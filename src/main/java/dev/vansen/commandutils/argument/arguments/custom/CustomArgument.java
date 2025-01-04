package dev.vansen.commandutils.argument.arguments.custom;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.exceptions.CmdSyntaxException;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Class representing a argument, which is a custom argument type.
 * This may be used to easily parse the value, or convert the native value to the target type.
 *
 * @param <T> the target type,
 * @param <N>
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
    public @NotNull T convert(@NotNull N nativeValue) throws CommandSyntaxException {
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
    public abstract @NotNull T parseOrConvert(@NotNull N nativeValue) throws CommandSyntaxException;

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        return suggest(new SuggestionsBuilderWrapper(builder));
    }

    /**
     * Suggests possible values for the custom argument.
     *
     * @param wrapper the suggestions builder wrapper
     * @return a {@link CompletableFuture} that will be completed with the {@link Suggestions} for tab completion.
     */
    public abstract @NotNull CompletableFuture<Suggestions> suggest(@NotNull SuggestionsBuilderWrapper wrapper);
}