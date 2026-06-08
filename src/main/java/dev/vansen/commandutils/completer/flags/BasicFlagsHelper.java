package dev.vansen.commandutils.completer.flags;

import dev.vansen.commandutils.completer.SuggestionsBuilderWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Smart helper for completing key/value flags inside a GREEDY string argument.
 *
 * <p>This helper is designed specifically for Brigadier greedy arguments and
 * handles offsets, spaces, and token boundaries correctly.</p>
 *
 * <p>This class provides a high-level, opinionated completion model.
 * Lower-level and more flexible alternatives may be introduced in future versions.</p>
 *
 * <h3>Supported syntaxes</h3>
 * <ul>
 *     <li>{@code --key}</li>
 *     <li>{@code --key value}</li>
 *     <li>Multiple flags in a single greedy argument</li>
 * </ul>
 */
@SuppressWarnings("unused")
public final class BasicFlagsHelper {

    private final SuggestionsBuilderWrapper wrapper;
    private final String input;
    private final String prefix;

    private final Map<String, Supplier<Collection<String>>> valueSuppliers = new LinkedHashMap<>();

    private final Set<String> multiUseFlags = new HashSet<>();

    private boolean multipleFlags = false;
    private int maxFlags = -1;

    /**
     * Creates a new flags helper bound to the given suggestions builder.
     *
     * @param wrapper the suggestions builder wrapper
     * @param prefix  the flag prefix (for example {@code "--"})
     */
    public BasicFlagsHelper(@NotNull SuggestionsBuilderWrapper wrapper, @NotNull String prefix) {
        this.wrapper = wrapper;
        this.input = wrapper.currentArg();
        this.prefix = prefix;
    }

    /**
     * Creates a new flags helper with the default prefix {@code "--"}.
     *
     * @param wrapper the suggestions builder wrapper
     */
    public BasicFlagsHelper(@NotNull SuggestionsBuilderWrapper wrapper) {
        this(wrapper, "--");
    }

    /**
     * Static factory method for creating a new flags helper, using the default prefix {@code "--"}.
     *
     * @param wrapper the suggestions builder wrapper
     * @return a new BasicFlagsHelper instance
     */
    public static BasicFlagsHelper of(@NotNull SuggestionsBuilderWrapper wrapper) {
        return new BasicFlagsHelper(wrapper);
    }

    /**
     * Static factory method for creating a new flags helper with a custom prefix.
     *
     * @param wrapper the suggestions builder wrapper
     * @param prefix  the flag prefix (for example {@code "--"})
     * @return a new BasicFlagsHelper instance
     */
    public static BasicFlagsHelper of(@NotNull SuggestionsBuilderWrapper wrapper, @NotNull String prefix) {
        return new BasicFlagsHelper(wrapper, prefix);
    }

    /**
     * Registers a flag that does not accept any values.
     *
     * @param key the flag name (without prefix)
     * @return this helper for chaining
     */
    @NotNull
    public BasicFlagsHelper registerKey(@NotNull String key) {
        valueSuppliers.put(key, null);
        return this;
    }

    /**
     * Registers a flag that accepts values.
     *
     * @param key      the flag name (without prefix)
     * @param supplier supplier providing possible values
     * @return this helper for chaining
     */
    @NotNull
    public BasicFlagsHelper registerKey(@NotNull String key, @NotNull Supplier<Collection<String>> supplier) {
        valueSuppliers.put(key, supplier);
        return this;
    }

    /**
     * Enables or disables allowing multiple flags globally.
     *
     * <p>If disabled (default), flags are suggested only once unless explicitly allowed.</p>
     *
     * @param enabled whether all flags can be used multiple times
     * @return this helper for chaining
     */
    @NotNull
    public BasicFlagsHelper multipleFlags(boolean enabled) {
        this.multipleFlags = enabled;
        return this;
    }

    /**
     * Toggles allowing multiple flags.
     *
     * <p>If disabled (default), flags are suggested only once unless explicitly allowed.</p>
     *
     * @return this helper for chaining
     */
    public BasicFlagsHelper multipleFlags() {
        return multipleFlags(!multipleFlags);
    }

    /**
     * Allows a specific flag to be used multiple times.
     *
     * @param key the flag name (without prefix)
     * @return this helper for chaining
     */
    @NotNull
    public BasicFlagsHelper allowMultiple(@NotNull String key) {
        this.multiUseFlags.add(key);
        return this;
    }

    /**
     * Sets the maximum number of flags allowed in the argument.
     *
     * @param max maximum number of flags, or {@code -1} for unlimited
     * @return this helper for chaining
     */
    @NotNull
    public BasicFlagsHelper maxFlags(int max) {
        this.maxFlags = max;
        return this;
    }

    /**
     * Applies suggestions based on the current greedy input and returns
     * the {@link SuggestionsBuilderWrapper} used for those suggestions.
     *
     * @return the builder used for suggesting
     */
    @NotNull
    public SuggestionsBuilderWrapper suggest() {
        String trimmed = input.stripLeading();

        if (trimmed.isEmpty()) {
            SuggestionsBuilderWrapper builder = wrapper.offset(wrapper.start());
            suggestAllKeys(List.of(), "", extractUsedKeys(List.of()), builder);
            return builder;
        }

        List<String> parts = new ArrayList<>(List.of(trimmed.split(" ")));
        boolean endsWithSpace = trimmed.endsWith(" ");

        String current = endsWithSpace ? "" : parts.removeLast();
        Set<String> usedKeys = extractUsedKeys(parts);

        if (maxFlags >= 0 && usedKeys.size() >= maxFlags) {
            return wrapper;
        }

        if (endsWithSpace) {
            SuggestionsBuilderWrapper builder = wrapper.offset(cursorOffset());

            if (!parts.isEmpty() && lastTokenExpectsValue(parts)) {
                suggestValue(parts, "", builder);
            } else {
                suggestAllKeys(parts, "", usedKeys, builder);
            }

            return builder;
        }

        if (!parts.isEmpty() && lastTokenExpectsValue(parts)) {
            SuggestionsBuilderWrapper builder = wrapper.offset(currentTokenOffset(parts) + parts.getLast().length() + 1);

            suggestValue(parts, current, builder);
            return builder;
        }

        if (current.startsWith(prefix)) {
            SuggestionsBuilderWrapper builder = wrapper.offset(cursorOffset() - current.length());
            suggestAllKeys(parts, current, usedKeys, builder);
            return builder;
        }

        SuggestionsBuilderWrapper builder = wrapper.offset(currentTokenOffset(parts) + (parts.isEmpty() ? "" : parts.getLast()).length() + 1);

        suggestValueOrNextKey(parts, current, usedKeys, builder);
        return builder;
    }

    /**
     * Determines whether the last completed token expects a value.
     */
    private boolean lastTokenExpectsValue(@NotNull List<String> parts) {
        String last = parts.getLast();
        if (!last.startsWith(prefix)) return false;

        String key = extractKey(last);
        return valueSuppliers.get(key) != null;
    }

    /**
     * Suggests all valid flag keys at the given position.
     */
    private void suggestAllKeys(
            @NotNull List<String> parts,
            @NotNull String current,
            @NotNull Set<String> usedKeys,
            @NotNull SuggestionsBuilderWrapper builder
    ) {
        String typed = current.length() >= prefix.length()
                ? current.substring(prefix.length())
                : "";

        for (String key : valueSuppliers.keySet()) {
            if (!key.startsWith(typed)) continue;
            if (!canSuggestFlag(key, usedKeys)) continue;
            builder.suggest(prefix + key);
        }
    }

    /**
     * Suggests values for the last flag.
     */
    private void suggestValue(@NotNull List<String> parts, @NotNull String current, @NotNull SuggestionsBuilderWrapper builder) {
        String last = parts.getLast();
        String key = extractKey(last);

        Supplier<Collection<String>> supplier = valueSuppliers.get(key);
        if (supplier == null) return;

        for (String value : supplier.get()) {
            if (!current.isEmpty() && !value.startsWith(current)) continue;
            builder.suggest(value);
        }
    }

    /**
     * Suggests either values for the last flag or new flags if the last
     * token does not accept values.
     */
    private void suggestValueOrNextKey(@NotNull List<String> parts, @NotNull String current, @NotNull Set<String> usedKeys, @NotNull SuggestionsBuilderWrapper builder) {
        if (parts.isEmpty()) {
            suggestAllKeys(parts, current, usedKeys, builder);
            return;
        }
        String last = parts.getLast();
        if (!last.startsWith(prefix)) {
            suggestAllKeys(parts, current, usedKeys, builder);
            return;
        }

        String key = extractKey(last);
        Supplier<Collection<String>> supplier = valueSuppliers.get(key);

        if (supplier == null) {
            suggestAllKeys(parts, current, usedKeys, builder);
            return;
        }

        for (String value : supplier.get()) {
            if (!value.startsWith(current)) continue;
            builder.suggest(value);
        }
    }

    /**
     * Determines whether a flag can be suggested based on usage rules.
     */
    private boolean canSuggestFlag(@NotNull String key, @NotNull Set<String> usedKeys) {
        if (multipleFlags) return true;
        if (multiUseFlags.contains(key)) return true;
        return !usedKeys.contains(key);
    }

    /**
     * Extracts all used flag keys from the parsed parts.
     */
    @NotNull
    private Set<String> extractUsedKeys(@NotNull List<String> parts) {
        Set<String> used = new HashSet<>();

        for (String part : parts) {
            if (!part.startsWith(prefix)) continue;
            used.add(extractKey(part));
        }

        return used;
    }

    /**
     * Extracts the key name from a flag token.
     */
    @NotNull
    private String extractKey(@NotNull String token) {
        int eq = token.indexOf('=');
        return eq == -1
                ? token.substring(prefix.length())
                : token.substring(prefix.length(), eq);
    }

    /**
     * Computes the offset at the current cursor position.
     */
    private int cursorOffset() {
        return wrapper.start() + input.length();
    }

    /**
     * Computes the offset of the currently edited token.
     */
    private int currentTokenOffset(@NotNull List<String> parts) {
        if (parts.isEmpty()) {
            return wrapper.start();
        }

        String last = parts.getLast();
        int index = input.lastIndexOf(last);
        return index == -1 ? wrapper.start() : wrapper.start() + index;
    }
}