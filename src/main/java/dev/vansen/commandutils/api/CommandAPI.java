package dev.vansen.commandutils.api;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for managing and accessing the lifecycle event manager for command registration.
 * <p>
 * This class provides static methods to get and set the {@link LifecycleEventManager} instance,
 * which is essential for registering commands and handling related events.
 * </p>
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class CommandAPI {
    private static LifecycleEventManager<Plugin> event;

    /**
     * Retrieves the current {@link LifecycleEventManager} instance.
     *
     * @return the {@link LifecycleEventManager} used for handling lifecycle events.
     * @throws RuntimeException if the event manager has not been set.
     */
    @NotNull
    public static LifecycleEventManager<Plugin> get() {
        if (event == null) throw new RuntimeException("Lifecycle event manager is not set!");
        return event;
    }

    /**
     * Sets the {@link LifecycleEventManager} instance to be used for handling lifecycle events.
     *
     * @param event the {@link LifecycleEventManager} instance to set.
     * @throws NullPointerException if the provided {@link LifecycleEventManager} is {@code null}.
     */
    public static void set(@NotNull LifecycleEventManager<Plugin> event) {
        CommandAPI.event = event;
    }
}