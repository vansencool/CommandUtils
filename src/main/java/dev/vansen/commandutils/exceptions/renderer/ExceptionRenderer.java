package dev.vansen.commandutils.exceptions.renderer;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Renders a throwable to a command sender.
 */
@FunctionalInterface
public interface ExceptionRenderer {

    /**
     * Sends the throwable to the sender.
     *
     * @param sender the command sender
     * @param throwable the throwable
     */
    void render(@NotNull CommandSender sender, @NotNull Throwable throwable);
}
