package dev.vansen.commandutils.api;

import com.mojang.brigadier.CommandDispatcher;
import dev.vansen.commandutils.argument.arguments.ColorArgumentType;
import dev.vansen.commandutils.argument.arguments.color.ArgumentColors;
import dev.vansen.commandutils.exceptions.APINotFoundException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for managing and accessing the lifecycle event manager for command registration.
 * <p>
 * This class provides static methods to get and set the {@link LifecycleEventManager} instance,
 * which is essential for registering commands.
 * </p>
 * This class also provides access to the {@link CommandDispatcher} once the event manager is set.
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class CommandAPI {
    private static LifecycleEventManager<@NotNull Plugin> event;
    private static CommandDispatcher<CommandSourceStack> dispatcher;

    /**
     * Retrieves the current {@link LifecycleEventManager} instance.
     *
     * @return the {@link LifecycleEventManager} used for handling lifecycle events.
     * @throws APINotFoundException if the event manager has not been set.
     */
    @NotNull
    public static LifecycleEventManager<@NotNull Plugin> get() {
        if (event == null) throw new APINotFoundException();
        return event;
    }

    /**
     * Sets the {@link LifecycleEventManager} instance to be used for handling lifecycle events, as well as the dispatcher.
     *
     * @param event the {@link LifecycleEventManager} instance to set.
     */
    public static void set(@NotNull LifecycleEventManager<@NotNull Plugin> event) {
        CommandAPI.event = event;
        event.registerEventHandler(LifecycleEvents.COMMANDS, registrar -> dispatcher = registrar.registrar().getDispatcher());
    }

    /**
     * Sets the {@link LifecycleEventManager} instance to be used for handling lifecycle events, as well as the dispatcher.
     *
     * @param plugin the {@link JavaPlugin} instance to set (uses the plugin's lifecycle event manager).
     */
    public static void set(@NotNull JavaPlugin plugin) {
        set(plugin.getLifecycleManager());
    }

    /**
     * Retrieves the {@link CommandDispatcher} instance.
     *
     * @return the {@link CommandDispatcher} used for handling commands.
     * @throws APINotFoundException if the dispatcher has not been set.
     */
    public static CommandDispatcher<CommandSourceStack> dispatcher() {
        if (dispatcher == null) throw new APINotFoundException();
        return dispatcher;
    }

    /**
     * Initializes the default colors for {@link ColorArgumentType}
     * <p>
     * This isn't required, just optional "0ms" of performance boost when using ArgumentColors for the first time (usually when somebody executes a command, and you are using {@link ColorArgumentType})
     */
    public static void initializeColors() {
        ArgumentColors.defaultColors();
    }
}