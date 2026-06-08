package dev.vansen.commandutils.expansion.bukkit.tabcompletion;

import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import dev.vansen.commandutils.expansion.bukkit.argument.BukkitArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A tab completer that provides suggestions for a command.
 */
@SuppressWarnings("unused")
public interface TabCompleter {

    @Nullable
    List<String> tabComplete(@NotNull BukkitArgument argument, @NotNull SuggestionsBuilderWrapper wrapper);
}
