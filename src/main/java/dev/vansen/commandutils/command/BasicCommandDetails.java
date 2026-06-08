package dev.vansen.commandutils.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public record BasicCommandDetails(CommandSourceStack context) {

    /**
     * Gets the sender.
     *
     * @return the sender.
     */
    public CommandSender sender() {
        return context.getSender();
    }

    /**
     * Gets the player, will throw an exception if the sender is not a player.
     *
     * @return the player.
     */
    public Player player() {
        return (Player) sender();
    }

    /**
     * Checks if the sender is a player.
     *
     * @return true if the sender is a player, false otherwise.
     */
    public boolean isPlayer() {
        return sender() instanceof Player;
    }

    /**
     * Gets the entity, will throw an exception if the sender is not an entity.
     *
     * @return the entity.
     */
    public Entity entity() {
        return context.getExecutor();
    }

    /**
     * Gets the location of the sender.
     *
     * @return the location.
     */
    public Location location() {
        return context.getLocation();
    }

    /**
     * Gets the world of the sender.
     *
     * @return the world.
     */
    public World world() {
        return location().getWorld();
    }

    /**
     * Checks if the sender has the given permission.
     *
     * @param permission the permission to check.
     * @return true if the sender has the permission, false otherwise.
     */
    public boolean hasPermission(String permission) {
        return sender().hasPermission(permission);
    }
}
