package dev.vansen.commandutils.api;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for managing and accessing the lifecycle event manager for command registration.
 * <p>
 * This class provides static methods to get and set the {@link LifecycleEventManager} instance,
 * which is essential for registering commands.
 * </p>
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class CommandAPI {
    private static LifecycleEventManager<@NotNull Plugin> event;

    /**
     * Retrieves the current {@link LifecycleEventManager} instance.
     *
     * @return the {@link LifecycleEventManager} used for handling lifecycle events.
     * @throws RuntimeException if the event manager has not been set.
     */
    @NotNull
    public static LifecycleEventManager<@NotNull Plugin> get() {
        if (event == null) throw new RuntimeException("Lifecycle event manager is not set!");
        return event;
    }

    /**
     * Sets the {@link LifecycleEventManager} instance to be used for handling lifecycle events.
     *
     * @param event the {@link LifecycleEventManager} instance to set.
     */
    public static void set(@NotNull LifecycleEventManager<@NotNull Plugin> event) {
        CommandAPI.event = event;
    }

    /**
     * Sets the {@link LifecycleEventManager} instance to be used for handling lifecycle events.
     *
     * @param plugin the {@link JavaPlugin} instance to set.
     */
    public static void set(@NotNull JavaPlugin plugin) {
        CommandAPI.event = plugin.getLifecycleManager();
    }
}