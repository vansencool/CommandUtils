package dev.vansen.commandutils.argument.finder;

import com.mojang.brigadier.arguments.*;
import dev.vansen.commandutils.argument.CommandArgument;
import dev.vansen.commandutils.argument.arguments.ColorArgumentType;
import dev.vansen.commandutils.argument.arguments.CommandBlockModeArgumentType;
import dev.vansen.commandutils.argument.arguments.PlayerArgumentType;
import dev.vansen.commandutils.exceptions.UnknownArgumentException;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A utility class for finding arguments from strings ({@link #fromString(String)})
 * <p>
 * Note, this checks for specific types of arguments, it is generally recommended to use {@link CommandArgument#of(String, ArgumentType)} if you want to use other types of arguments.
 */
@SuppressWarnings("UnstableApiUsage")
public class ArgumentString {

    /**
     * A list of all argument types that can be parsed.
     * <p>
     * Supports: string, greedy, word, int, float, double, boolean, long, player, entity, blockpos, blockstate, color, commandblockmode, players, entities, namedcolor, world, gamemode, itemstack, uuid
     */
    @NotNull
    public static List<String> types = ObjectArrayList.of(
            "string",
            "greedy",
            "word",
            "int",
            "float",
            "double",
            "boolean",
            "long",
            "player",
            "entity",
            "blockpos",
            "blockstate",
            "color",
            "commandblockmode",
            "players",
            "entities",
            "namedcolor",
            "world",
            "gamemode",
            "itemstack",
            "uuid"
    );

    /**
     * Gets an argument type from a string.
     * View more details at {@link #types}
     *
     * @param string the string to get the argument type from
     * @return the argument type
     * @throws UnknownArgumentException if the string is not a valid argument type
     */
    @NotNull
    public static ArgumentType<?> fromString(@NotNull String string) {
        return switch (string.toLowerCase()) {
            case "string" -> StringArgumentType.string();
            case "greedy" -> StringArgumentType.greedyString();
            case "word" -> StringArgumentType.word();
            case "int" -> IntegerArgumentType.integer();
            case "float" -> FloatArgumentType.floatArg();
            case "double" -> DoubleArgumentType.doubleArg();
            case "boolean" -> BoolArgumentType.bool();
            case "long" -> LongArgumentType.longArg();
            case "player" -> PlayerArgumentType.player();
            case "entity" -> ArgumentTypes.entity();
            case "blockpos" -> ArgumentTypes.blockPosition();
            case "blockstate" -> ArgumentTypes.blockState();
            case "color" -> ColorArgumentType.color();
            case "commandblockmode" -> CommandBlockModeArgumentType.mode();
            case "players" -> ArgumentTypes.players();
            case "entities" -> ArgumentTypes.entities();
            case "namedcolor" -> ArgumentTypes.namedColor();
            case "world" -> ArgumentTypes.world();
            case "gamemode" -> ArgumentTypes.gameMode();
            case "itemstack" -> ArgumentTypes.itemStack();
            case "uuid" -> ArgumentTypes.uuid();
            default -> throw new UnknownArgumentException(string, types);
        };
    }
}
