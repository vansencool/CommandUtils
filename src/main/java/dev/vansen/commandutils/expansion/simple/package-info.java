/**
 * Provides the {@code Simple} command expansion.
 *
 * <p>
 * The Simple expansion defines a class-based command model where commands are
 * declared by extending a base utility class and configuring all behavior during
 * construction. This approach is inspired by Minestom’s command system and favors
 * explicit structure over deeply nested or stateful builders.
 *
 * <p>
 * A command in this package is typically represented by a single class that
 * extends {@code SimpleCommandUtils}. Executors, arguments, subcommands, aliases,
 * sender restrictions, and metadata are defined directly in the constructor,
 * resulting in a clear and predictable command layout.
 */
package dev.vansen.commandutils.expansion.simple;