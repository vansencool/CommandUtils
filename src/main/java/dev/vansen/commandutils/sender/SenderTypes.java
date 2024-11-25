package dev.vansen.commandutils.sender;

import dev.vansen.commandutils.command.CommandWrapper;
import dev.vansen.commandutils.exceptions.CmdException;
import dev.vansen.commandutils.messages.MessageTypes;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public enum SenderTypes {

    /**
     * Represents a player sender {@link Player}.
     */
    PLAYER {
        @Override
        public void check(CommandWrapper context) {
            if (!context.isPlayer()) {
                throw new CmdException(MessageTypes.PLAYER_EXCEPTION, context.sender());
            }
        }

        @Override
        public void check(CommandWrapper context, String message) {
            if (!context.isPlayer()) {
                throw new CmdException(message, context.sender());
            }
        }
    },

    /**
     * Represents a console sender {@link ConsoleCommandSender}.
     */
    CONSOLE {
        @Override
        public void check(CommandWrapper context) {
            if (!context.isConsole()) {
                throw new CmdException(MessageTypes.CONSOLE_EXCEPTION, context.sender());
            }
        }

        @Override
        public void check(CommandWrapper context, String message) {
            if (!context.isConsole()) {
                throw new CmdException(message, context.sender());
            }
        }
    },

    /**
     * Represents a remote console sender {@link RemoteConsoleCommandSender}.
     */
    REMOTE_CONSOLE {
        @Override
        public void check(CommandWrapper context) {
            if (!context.isConsole()) {
                throw new CmdException(MessageTypes.REMOTE_CONSOLE_EXCEPTION, context.sender());
            }
        }

        @Override
        public void check(CommandWrapper context, String message) {
            if (!context.isConsole()) {
                throw new CmdException(message, context.sender());
            }
        }
    },

    /**
     * Represents an entity sender {@link Entity}.
     */
    ENTITY {
        @Override
        public void check(CommandWrapper context) {
            if (!context.isEntity()) {
                throw new CmdException(MessageTypes.ENTITY_EXCEPTION, context.sender());
            }
        }

        @Override
        public void check(CommandWrapper context, String message) {
            if (!context.isEntity()) {
                throw new CmdException(message, context.sender());
            }
        }
    },

    /**
     * Represents a command block sender {@link BlockCommandSender}.
     */
    COMMAND_BLOCK {
        @Override
        public void check(CommandWrapper context) {
            if (!context.isBlock()) {
                throw new CmdException(MessageTypes.COMMAND_BLOCK_EXCEPTION, context.sender());
            }
        }

        @Override
        public void check(CommandWrapper context, String message) {
            if (!context.isBlock()) {
                throw new CmdException(message, context.sender());
            }
        }
    },

    /**
     * Represents a proxied sender {@link ProxiedCommandSender}.
     */
    PROXIED {
        @Override
        public void check(CommandWrapper context) {
            if (!context.isProxied()) {
                throw new CmdException(MessageTypes.PROXIED_SENDER_EXCEPTION, context.sender());
            }
        }

        @Override
        public void check(CommandWrapper context, String message) {
            if (!context.isProxied()) {
                throw new CmdException(message, context.sender());
            }
        }
    },

    /**
     * Represents an unknown command sender.
     */
    UNKNOWN;

    public void check(CommandWrapper context) {
    }

    public void check(CommandWrapper context, String message) {
    }
}