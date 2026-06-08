package dev.vansen.commandutils.expansion.bukkit.command;

import dev.vansen.commandutils.expansion.bukkit.argument.BukkitArgument;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides the executor for a command.
 */
@SuppressWarnings("unused")
public interface BukkitCommands {
    void execute(@NotNull BukkitArgument argument);
}