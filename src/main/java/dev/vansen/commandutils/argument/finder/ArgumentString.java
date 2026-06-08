package dev.vansen.commandutils.argument.finder;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.argument.arguments.ColorArgumentType;
import dev.vansen.commandutils.argument.arguments.CommandBlockModeArgumentType;
import dev.vansen.commandutils.argument.arguments.PlayerArgumentType;
import dev.vansen.commandutils.exceptions.UnknownArgumentException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A utility class for resolving argument types from string identifiers.
 *
 * <p>
 * This utility supports a predefined set of commonly used argument types and allows
 * registering or unregistering custom argument types at runtime.
 * </p>
 *
 * <p>
 * For custom or unsupported argument types, {@link CommandArgument#of(String, ArgumentType)}
 * may also be used directly.
 * </p>
 */
@SuppressWarnings({"UnstableApiUsage", "unused"})
public final class ArgumentString {

    /**
     * Registry of argument identifiers to their corresponding argument types.
     */
    @NotNull
    private static final Map<String, ArgumentType<?>> TYPES = new ConcurrentHashMap<>();

    static {
        TYPES.put("string", StringArgumentType.string());
        TYPES.put("greedy", StringArgumentType.greedyString());
        TYPES.put("word", StringArgumentType.word());
        TYPES.put("int", IntegerArgumentType.integer());
        TYPES.put("float", FloatArgumentType.floatArg());
        TYPES.put("double", DoubleArgumentType.doubleArg());
        TYPES.put("boolean", BoolArgumentType.bool());
        TYPES.put("long", LongArgumentType.longArg());
        TYPES.put("player", PlayerArgumentType.player());
        TYPES.put("entity", ArgumentTypes.entity());
        TYPES.put("blockpos", ArgumentTypes.blockPosition());
        TYPES.put("blockstate", ArgumentTypes.blockState());
        TYPES.put("color", ColorArgumentType.color());
        TYPES.put("commandblockmode", CommandBlockModeArgumentType.mode());
        TYPES.put("players", ArgumentTypes.players());
        TYPES.put("entities", ArgumentTypes.entities());
        TYPES.put("namedcolor", ArgumentTypes.namedColor());
        TYPES.put("world", ArgumentTypes.world());
        TYPES.put("gamemode", ArgumentTypes.gameMode());
        TYPES.put("itemstack", ArgumentTypes.itemStack());
        TYPES.put("uuid", ArgumentTypes.uuid());
    }

    /**
     * Resolves an argument type from its string identifier.
     *
     * @param string the identifier to resolve
     * @return the corresponding argument type
     * @throws UnknownArgumentException if the identifier is not registered
     */
    @NotNull
    public static ArgumentType<?> fromString(@NotNull String string) {
        ArgumentType<?> type = TYPES.get(string.toLowerCase());
        if (type == null) {
            throw new UnknownArgumentException(string, TYPES.keySet());
        }
        return type;
    }

    /**
     * Registers a new argument type.
     *
     * @param name the identifier to register
     * @param type the argument type
     * @throws IllegalStateException if the identifier is already registered
     */
    public static void register(@NotNull String name, @NotNull ArgumentType<?> type) {
        String key = name.toLowerCase();
        if (TYPES.putIfAbsent(key, type) != null) {
            throw new IllegalStateException("Argument type already registered: " + key);
        }
    }

    /**
     * Unregisters an argument type.
     *
     * @param name the identifier to unregister
     * @return the removed argument type, or {@code null} if none was registered
     */
    @Nullable
    public static ArgumentType<?> unregister(@NotNull String name) {
        return TYPES.remove(name.toLowerCase());
    }

    private ArgumentString() {
        throw new UnsupportedOperationException();
    }
}
