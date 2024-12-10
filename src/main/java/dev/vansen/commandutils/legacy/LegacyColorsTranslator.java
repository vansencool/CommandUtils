package dev.vansen.commandutils.legacy;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * A utility class for converting legacy color codes and hex color codes (including bungee's hex codes) into MiniMessage.
 */
public class LegacyColorsTranslator {

    /**
     * Converts legacy color codes and hex color codes (including bungee's hex codes) into MiniMessage format.
     */
    public static @NotNull String translate(@NotNull String input) {
        return Pattern.compile("(?i)[&§]([k-or])").matcher(Pattern.compile("(?i)[&§]([0-9a-f])").matcher(Pattern.compile("(?i)&x((&[a-f0-9]){6})").matcher(input.replaceAll("(?i)&?#([a-f0-9]{6})", "<#$1>"))
                                .replaceAll(match -> "<#" + match.group(0).replace("&", "").substring(1) + ">"))
                        .replaceAll(match -> new String[]{
                                "<black>", "<dark_blue>", "<dark_green>", "<dark_aqua>", "<dark_red>",
                                "<dark_purple>", "<gold>", "<gray>", "<dark_gray>", "<blue>",
                                "<green>", "<aqua>", "<red>", "<light_purple>", "<yellow>", "<white>"
                        }[Integer.parseInt(match.group(1), 16)]))
                .replaceAll(match -> {
                    int formatIndex = "klmnor".indexOf(Character.toLowerCase(match.group(1).charAt(0)));
                    return formatIndex != -1 ? new String[]{"<obfuscated>", "<bold>", "<strikethrough>", "<underlined>", "<italic>", "<reset>"}[formatIndex] : "";
                })
                .replaceAll("&x", "");
    }
}