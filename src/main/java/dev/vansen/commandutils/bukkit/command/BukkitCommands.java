package dev.vansen.commandutils.bukkit.command;

import dev.vansen.commandutils.bukkit.argument.BukkitArgument;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides the executor for a command.
 */
@SuppressWarnings("unused")
public interface BukkitCommands {
    void execute(@NotNull BukkitArgument argument);
}