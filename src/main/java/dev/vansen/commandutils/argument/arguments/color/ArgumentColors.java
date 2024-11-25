package dev.vansen.commandutils.argument.arguments.color;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ArgumentColors {

    /**
     * A map of color names to their hex values, this contains all colors from {@link ArgumentColors#colorNames}
     */
    public static Map<String, String> COLOR_MAP = new HashMap<>(1024, 0.75f);

    /**
     * A list of many color names.
     */
    public static ConcurrentLinkedDeque<String> colorNames = new ConcurrentLinkedDeque<>(
            List.of(
                    "black",
                    "dark_blue",
                    "dark_green",
                    "dark_aqua",
                    "dark_red",
                    "dark_purple",
                    "gold",
                    "gray",
                    "dark_gray",
                    "blue",
                    "green",
                    "aqua",
                    "red",
                    "light_purple",
                    "yellow",
                    "white",
                    "navy",
                    "teal",
                    "olive",
                    "maroon",
                    "lime",
                    "orange",
                    "fuchsia",
                    "silver",
                    "indigo",
                    "pink",
                    "cyan",
                    "beige",
                    "peach",
                    "lavender",
                    "mint",
                    "coral",
                    "amber",
                    "charcoal",
                    "chocolate",
                    "crimson",
                    "khaki",
                    "magenta",
                    "plum",
                    "scarlet",
                    "turquoise",
                    "emerald",
                    "ruby",
                    "jade",
                    "sapphire",
                    "bronze",
                    "goldenrod",
                    "steel_blue",
                    "salmon",
                    "ivory",
                    "honeydew",
                    "periwinkle",
                    "azure",
                    "powder_blue",
                    "rosy_brown",
                    "pale_green",
                    "orchid",
                    "midnight_blue",
                    "slate_gray",
                    "sea_green",
                    "forest_green",
                    "dim_gray",
                    "cadet_blue",
                    "firebrick",
                    "golden_yellow",
                    "steel",
                    "violet",
                    "eggshell",
                    "apricot",
                    "mustard",
                    "blush",
                    "sepia",
                    "cerulean",
                    "vermilion",
                    "chartreuse",
                    "prussian_blue",
                    "spring_green",
                    "golden_rod",
                    "burnt_orange",
                    "eggplant",
                    "ultramarine",
                    "carmine",
                    "heliotrope",
                    "persian_blue",
                    "tangerine",
                    "celadon",
                    "mauve",
                    "cerise",
                    "cobalt",
                    "pearl"
            )
    );

    static {
        addColor(
                Map.entry("black", "#000000"),
                Map.entry("dark_blue", "#0000aa"),
                Map.entry("dark_green", "#00aa00"),
                Map.entry("dark_aqua", "#00aaaa"),
                Map.entry("dark_red", "#aa0000"),
                Map.entry("dark_purple", "#aa00aa"),
                Map.entry("gold", "#ffaa00"),
                Map.entry("gray", "#aaaaaa"),
                Map.entry("dark_gray", "#555555"),
                Map.entry("blue", "#5555ff"),
                Map.entry("green", "#55ff55"),
                Map.entry("aqua", "#55ffff"),
                Map.entry("red", "#ff5555"),
                Map.entry("light_purple", "#ff55ff"),
                Map.entry("yellow", "#ffff55"),
                Map.entry("white", "#ffffff"),
                Map.entry("navy", "#001f3f"),
                Map.entry("teal", "#39cccc"),
                Map.entry("olive", "#3d9970"),
                Map.entry("maroon", "#85144b"),
                Map.entry("lime", "#01ff70"),
                Map.entry("orange", "#ff851b"),
                Map.entry("fuchsia", "#f012be"),
                Map.entry("silver", "#dddddd"),
                Map.entry("indigo", "#4b0082"),
                Map.entry("pink", "#ffc0cb"),
                Map.entry("cyan", "#00ffff"),
                Map.entry("beige", "#f5f5dc"),
                Map.entry("peach", "#ffdab9"),
                Map.entry("lavender", "#e6e6fa"),
                Map.entry("mint", "#98ff98"),
                Map.entry("coral", "#ff7f50"),
                Map.entry("amber", "#ffbf00"),
                Map.entry("charcoal", "#36454f"),
                Map.entry("chocolate", "#d2691e"),
                Map.entry("crimson", "#dc143c"),
                Map.entry("khaki", "#f0e68c"),
                Map.entry("magenta", "#ff00ff"),
                Map.entry("plum", "#dda0dd"),
                Map.entry("scarlet", "#ff2400"),
                Map.entry("turquoise", "#40e0d0"),
                Map.entry("emerald", "#50c878"),
                Map.entry("ruby", "#e0115f"),
                Map.entry("jade", "#00a86b"),
                Map.entry("sapphire", "#0f52ba"),
                Map.entry("bronze", "#cd7f32"),
                Map.entry("goldenrod", "#daa520"),
                Map.entry("steel_blue", "#4682b4"),
                Map.entry("salmon", "#fa8072"),
                Map.entry("ivory", "#fffff0"),
                Map.entry("honeydew", "#f0fff0"),
                Map.entry("periwinkle", "#ccccff"),
                Map.entry("azure", "#f0ffff"),
                Map.entry("powder_blue", "#b0e0e6"),
                Map.entry("rosy_brown", "#bc8f8f"),
                Map.entry("pale_green", "#98fb98"),
                Map.entry("orchid", "#da70d6"),
                Map.entry("midnight_blue", "#191970"),
                Map.entry("slate_gray", "#708090"),
                Map.entry("sea_green", "#2e8b57"),
                Map.entry("forest_green", "#228b22"),
                Map.entry("dim_gray", "#696969"),
                Map.entry("cadet_blue", "#5f9ea0"),
                Map.entry("firebrick", "#b22222"),
                Map.entry("golden_yellow", "#ffdf00"),
                Map.entry("steel", "#b0c4de"),
                Map.entry("violet", "#ee82ee"),
                Map.entry("eggshell", "#f0ead6"),
                Map.entry("apricot", "#fbceb1"),
                Map.entry("mustard", "#ffdb58"),
                Map.entry("blush", "#de5d83"),
                Map.entry("sepia", "#704214"),
                Map.entry("cerulean", "#007ba7"),
                Map.entry("vermilion", "#e34234"),
                Map.entry("chartreuse", "#7fff00"),
                Map.entry("prussian_blue", "#003153"),
                Map.entry("spring_green", "#00ff7f"),
                Map.entry("golden_rod", "#daa520"),
                Map.entry("burnt_orange", "#cc5500"),
                Map.entry("eggplant", "#614051"),
                Map.entry("ultramarine", "#3f00ff"),
                Map.entry("carmine", "#960018"),
                Map.entry("heliotrope", "#df73ff"),
                Map.entry("persian_blue", "#1c39bb"),
                Map.entry("tangerine", "#f28500"),
                Map.entry("celadon", "#ace1af"),
                Map.entry("mauve", "#e0b0ff"),
                Map.entry("cerise", "#de3163"),
                Map.entry("cobalt", "#0047ab"),
                Map.entry("pearl", "#eae0c8")
        );
    }

    /**
     * Adds a color to the color map.
     *
     * @param name the name of the color
     * @param hex  the hex code of the color
     */
    public static void addColor(String name, String hex) {
        COLOR_MAP.put(name, hex);
    }

    /**
     * Adds multiple colors to the color map from one or more maps.
     *
     * @param maps one or more maps containing color names and hex codes
     */
    @SafeVarargs
    public static void addColor(@NotNull Map<String, String>... maps) {
        Arrays.stream(maps).forEach(map -> COLOR_MAP.putAll(map));
    }

    /**
     * Adds multiple colors to the color map from one or more entries.
     *
     * @param entries one or more entries containing color names and hex codes
     */
    @SafeVarargs
    public static void addColor(@NotNull Map.Entry<String, String>... entries) {
        addColor(Arrays.stream(entries)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    /**
     * Removes one or more colors from the color map.
     *
     * @param names the names of the colors to remove
     */
    public static void removeClear(@NotNull String... names) {
        Arrays.stream(names)
                .forEach(COLOR_MAP::remove);
    }

    /**
     * Clears all colors from the color map.
     */
    public static void clearColors() {
        COLOR_MAP.clear();
    }
}
