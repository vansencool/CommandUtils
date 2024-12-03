package dev.vansen.commandutils.argument.arguments.color;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ArgumentColors {

    /**
     * A map of color names to their hex values, check {@link #defaultColors()} for default colors and {@link #addColor(String, String)} for adding custom colors
     */
    public static ConcurrentMap<String, String> COLOR_MAP = new ConcurrentHashMap<>(1024, 0.75f);

    static {
        defaultColors();
    }

    public static void defaultColors() {
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
                Map.entry("pearl", "#eae0c8"),
                Map.entry("amethyst", "#9966cc"),
                Map.entry("antique_white", "#faebd7"),
                Map.entry("bistre", "#3d2b1f"),
                Map.entry("cerulean_blue", "#2a52be"),
                Map.entry("cinnamon", "#d2691e"),
                Map.entry("copper", "#b87333"),
                Map.entry("cornflower_blue", "#6495ed"),
                Map.entry("denim", "#1560bd"),
                Map.entry("electric_lime", "#ccff00"),
                Map.entry("feldspar", "#d19275"),
                Map.entry("gunmetal", "#2a3439"),
                Map.entry("indigo_dye", "#00416a"),
                Map.entry("iris", "#5a4fcf"),
                Map.entry("lapis_lazuli", "#26619c"),
                Map.entry("mahogany", "#c04000"),
                Map.entry("mint_green", "#98ff98"),
                Map.entry("neon_carrot", "#ffa343"),
                Map.entry("ocean_blue", "#4f42b5"),
                Map.entry("onyx", "#353839"),
                Map.entry("parchment", "#f1e9d2"),
                Map.entry("pastel_pink", "#dea5a4"),
                Map.entry("peacock", "#33a1c9"),
                Map.entry("persian_red", "#cc3333"),
                Map.entry("pine_green", "#01796f"),
                Map.entry("razzmatazz", "#e3256b"),
                Map.entry("saffron", "#f4c430"),
                Map.entry("sandy_brown", "#f4a460"),
                Map.entry("sepia_tone", "#704214"),
                Map.entry("smoky_black", "#100c08"),
                Map.entry("taupe", "#483c32"),
                Map.entry("teal_green", "#006d5b"),
                Map.entry("ultramarine_blue", "#4166f5"),
                Map.entry("verdigris", "#43b3ae"),
                Map.entry("veronica", "#a020f0"),
                Map.entry("viridian", "#40826d"),
                Map.entry("wild_blue_yonder", "#a2add0"),
                Map.entry("zinnwaldite", "#ebc2af"),
                Map.entry("wheat", "#f5deb3"),
                Map.entry("lemon_chiffon", "#fffacd"),
                Map.entry("moonstone", "#3aa8c1"),
                Map.entry("acid_green", "#b0bf1a"),
                Map.entry("aero_blue", "#c0e8d5"),
                Map.entry("alabaster", "#f2f0e6"),
                Map.entry("alice_blue", "#f0f8ff"),
                Map.entry("alloy_orange", "#c46210"),
                Map.entry("almond", "#efdecd"),
                Map.entry("asparagus", "#87a96b"),
                Map.entry("atomic_tangerine", "#ff9966"),
                Map.entry("azureish_white", "#dbe9f4"),
                Map.entry("banana_yellow", "#ffe135"),
                Map.entry("bittersweet", "#fe6f5e"),
                Map.entry("black_coral", "#54626f"),
                Map.entry("blue_bell", "#a2a2d0"),
                Map.entry("blue_gray", "#6699cc"),
                Map.entry("blue_sapphire", "#126180"),
                Map.entry("bright_ube", "#d19fe8"),
                Map.entry("bubble_gum", "#ffc1cc"),
                Map.entry("burnt_sienna", "#e97451"),
                Map.entry("byzantine", "#bd33a4"),
                Map.entry("candy_pink", "#e4717a"),
                Map.entry("cardinal_red", "#c41e3a"),
                Map.entry("carmine_pink", "#eb4c42"),
                Map.entry("carrot_orange", "#ed9121"),
                Map.entry("celestial_blue", "#4997d0"),
                Map.entry("champagne", "#f7e7ce"),
                Map.entry("cherry_blossom", "#ffb7c5"),
                Map.entry("chili_red", "#e23d28"),
                Map.entry("china_rose", "#a8516e"),
                Map.entry("citrine", "#e4d00a"),
                Map.entry("clover_green", "#009b77"),
                Map.entry("cool_gray", "#8c92ac"),
                Map.entry("cotton_candy", "#ffbcd9"),
                Map.entry("crayola_yellow", "#fce883"),
                Map.entry("cyber_grape", "#58427c"),
                Map.entry("cyber_yellow", "#ffd300"),
                Map.entry("dark_byzantium", "#5d3954"),
                Map.entry("dark_olive", "#556b2f"),
                Map.entry("deep_pink", "#ff1493"),
                Map.entry("ecru", "#c2b280"),
                Map.entry("electric_blue", "#7df9ff"),
                Map.entry("fawn", "#e5aa70"),
                Map.entry("fern_green", "#4f7942"),
                Map.entry("frostbite", "#e936a7"),
                Map.entry("gamboge", "#e49b0f"),
                Map.entry("ghost_white", "#f8f8ff"),
                Map.entry("glaucous", "#6082b6"),
                Map.entry("golden_poppy", "#fcc200"),
                Map.entry("granite_gray", "#676767"),
                Map.entry("honey_gold", "#d4af37"),
                Map.entry("hunter_green", "#355e3b"),
                Map.entry("iceberg", "#71a6d2"),
                Map.entry("isabelline", "#f4f0ec"),
                Map.entry("jasmine", "#f8de7e"),
                Map.entry("jelly_bean", "#da614e"),
                Map.entry("jungle_green", "#29ab87"),
                Map.entry("kelly_green", "#4cbb17"),
                Map.entry("lavender_gray", "#c4c3d0"),
                Map.entry("lemon_curry", "#cca01d"),
                Map.entry("lilac_luster", "#ae98aa"),
                Map.entry("linen", "#faf0e6"),
                Map.entry("lust", "#e62020"),
                Map.entry("manatee", "#979aaa"),
                Map.entry("mango_tango", "#ff8243"),
                Map.entry("midnight_green", "#004953"),
                Map.entry("mountbatten_pink", "#997a8d"),
                Map.entry("munsell_red", "#f2003c"),
                Map.entry("nadeshiko_pink", "#f6adc6"),
                Map.entry("naples_yellow", "#fada5e"),
                Map.entry("neon_blue", "#4666ff"),
                Map.entry("olive_drab", "#6b8e23"),
                Map.entry("onyx_black", "#353839"),
                Map.entry("ou_crimson", "#990000"),
                Map.entry("peach_puff", "#ffdab9"),
                Map.entry("pewter_blue", "#8ba8b7"),
                Map.entry("quartz", "#51484f"),
                Map.entry("redwood", "#a45a52")
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