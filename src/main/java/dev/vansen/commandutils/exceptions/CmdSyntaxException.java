package dev.vansen.commandutils.exceptions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class CmdSyntaxException extends CommandSyntaxException {

    /**
     * Creates a new syntax exception with the given message.
     *
     * @param message the message to include in the exception
     */
    public CmdSyntaxException(@NotNull String message) {
        super(new SimpleCommandExceptionType(MessageComponentSerializer.message().serializeOrNull(Component.text(message))), MessageComponentSerializer.message().serializeOrNull(Component.text(message)));
    }

    /**
     * Creates a new syntax exception with the given message.
     *
     * @param message the message to include in the exception
     */
    public CmdSyntaxException(@NotNull Component message) {
        super(new SimpleCommandExceptionType(MessageComponentSerializer.message().serializeOrNull(message)), MessageComponentSerializer.message().serializeOrNull(message));
    }
}