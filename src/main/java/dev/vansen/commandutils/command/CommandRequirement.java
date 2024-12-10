package dev.vansen.commandutils.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public record CommandRequirement(CommandSourceStack context) {

    /**
     * Gets the sender.
     *
     * @return the sender.
     */
    public CommandSender sender() {
        return context.getSender();
    }

    /**
     * Gets the player, this shouldn't throw an exception, since it's likely the sender is a player.
     *
     * @return the player.
     */
    public Player player() {
        return (Player) sender();
    }

    /**
     * Gets the entity, this shouldn't throw an exception, since it's likely the sender is an entity.
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
}
