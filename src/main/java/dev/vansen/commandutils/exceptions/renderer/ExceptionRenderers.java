package dev.vansen.commandutils.exceptions.renderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for custom exception renderers.
 */
@SuppressWarnings("unused")
public final class ExceptionRenderers {

    private static final Map<Class<? extends Throwable>, ExceptionRenderer> RENDERERS = new ConcurrentHashMap<>();

    private ExceptionRenderers() {}

    /**
     * Registers a renderer for an exception type.
     *
     * @param type the exception class
     * @param renderer the renderer
     */
    public static void register(@NotNull Class<? extends Throwable> type, @NotNull ExceptionRenderer renderer) {
        RENDERERS.put(type, renderer);
    }

    /**
     * Unregisters a renderer.
     *
     * @param type the exception class
     */
    public static void unregister(@NotNull Class<? extends Throwable> type) {
        RENDERERS.remove(type);
    }

    /**
     * Finds a renderer for the given throwable, walking the class hierarchy.
     *
     * @param throwable the throwable
     * @return the renderer or {@code null}
     */
    public static @Nullable ExceptionRenderer find(@NotNull Throwable throwable) {
        Class<?> c = throwable.getClass();
        while (c != null) {
            ExceptionRenderer renderer = RENDERERS.get(c);
            if (renderer != null) return renderer;
            c = c.getSuperclass();
        }
        return null;
    }
}
