/**
 * The Expansion package contains alternative command implementations that extend
 * or replace parts of the core command system.
 *
 * <p>
 * The main command API is centered around {@code CommandUtils},
 * {@code CommandArgument}, and {@code SubCommand}. While flexible, this system is
 * heavily builder-oriented, which can result in awkward writing styles, unclear
 * command structures, and unexpected argument or subcommand attachment in more
 * complex setups.
 *
 * <p>
 * Expansions exist to address these limitations by providing different approaches
 * to command definition, prioritizing clarity, structure, and ease of use over
 * strict builder patterns.
 *
 * <h2>Current Expansions</h2>
 *
 * <p>
 * Currently, there are two main expansions available, more may be added in the future:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b>Simple</b> – A lightweight and expressive model inspired by Minestom’s
 *     command syntax, designed for readability and predictable structure.
 *   </li>
 *   <li>
 *     <b>Bukkit</b> – This implementation is currently incomplete and will go through significant changes in the future, but it provides a familiar API for Bukkit plugin developers, while improving upon the original Bukkit command system's design flaws and limitations.
 *   </li>
 * </ul>
 *
 * <p>
 * This package is intentionally open-ended. Additional expansions, such as
 * annotation-based command definitions and other experimental or platform-specific
 * models, may be added in the future without impacting the core API.
 * </p>
 */
package dev.vansen.commandutils.expansion;