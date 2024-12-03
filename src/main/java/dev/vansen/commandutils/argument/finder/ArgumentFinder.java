package dev.vansen.commandutils.argument.finder;

import dev.vansen.commandutils.command.CommandWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A utility class for finding the value of a specific argument in a command context.
 * <p>
 * Note, this checks for specific types of arguments, it is generally recommended to use {@link CommandWrapper#arg(String, Class)} if you want basically any type of argument.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ArgumentFinder<T> {
    private final @NotNull AtomicReference<T> value = new AtomicReference<>();
    private static final ObjectArrayList<Class<?>> arguments = ObjectArrayList.of();
    private final @NotNull CommandWrapper context;
    private final @NotNull String arg;

    /**
     * Creates a new instance of the ArgumentFinder class.
     *
     * @param context the command context
     * @param arg     the argument to find and parse
     */
    public ArgumentFinder(@NotNull CommandWrapper context, @NotNull String arg) {
        this.context = context;
        this.arg = arg;
    }

    /**
     * Creates a new instance of the ArgumentFinder class.
     *
     * @param <T>     the type of the argument value
     * @param context the command context
     * @param arg     the argument to find and parse
     * @return a new instance of the ArgumentFinder class
     */
    public static <T> ArgumentFinder<T> of(@NotNull CommandWrapper context, @NotNull String arg) {
        return new ArgumentFinder<>(context, arg);
    }

    /**
     * Adds a new class type to the list of available arguments.
     *
     * @param types the class types to add
     */
    public static void addArgument(@NotNull Class<?>... types) {
        arguments.addAll(ObjectList.of(types));
    }

    /**
     * Tries to parse the argument as a string.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryString() {
        try {
            value.set((T) context.argString(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as an integer.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryInt() {
        try {
            value.set((T) (Integer) context.argInt(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a floating-point number.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryFloat() {
        try {
            value.set((T) (Float) context.argFloat(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a double-precision floating-point number.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryDouble() {
        try {
            value.set((T) (Double) context.argDouble(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a boolean value.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryBoolean() {
        try {
            value.set((T) (Boolean) context.argBoolean(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a long integer.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryLong() {
        try {
            value.set((T) (Long) context.argLong(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a game mode.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryGameMode() {
        try {
            value.set((T) context.argGameMode(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as an item stack.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryItemStack() {
        try {
            value.set((T) context.argItemStack(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a player.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryPlayer() {
        try {
            value.set((T) context.argPlayer(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a color.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryColor() {
        try {
            value.set((T) context.argColor(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument as a world.
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryWorld() {
        try {
            value.set((T) context.argWorld(arg));
        } catch (Exception ignored) {
        }
        return this;
    }

    /**
     * Tries to parse the argument in all of the {@link #arguments}
     *
     * @return this ArgumentFinder instance
     */
    public ArgumentFinder<T> tryCustoms() {
        arguments.forEach(clazz -> {
            try {
                value.set((T) context.arg(arg, clazz));
            } catch (Exception ignored) {
            }
        });
        return this;
    }

    /**
     * Returns the parsed value of the argument, if it was successfully parsed.
     *
     * @return the parsed value of the argument, or null if no value was parsed
     */
    public T get() {
        return value.get();
    }
}