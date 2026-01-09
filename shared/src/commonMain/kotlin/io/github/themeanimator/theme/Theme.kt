package io.github.themeanimator.theme

import androidx.compose.runtime.Immutable

/**
 * Represents the available theme modes for the application.
 *
 * This sealed class defines three theme modes: [Light], [Dark], and [System].
 * Each theme mode provides specific behavior for determining its opposite theme
 * and maintaining persistence through ordinal values.
 */
@Immutable
sealed class Theme {

    /**
     * Returns the opposite theme based on the current theme and system settings.
     *
     * @param isSystemInDarkTheme Whether the system is currently in dark theme mode.
     *                           This parameter is only relevant for [System] theme mode.
     * @return The opposite theme: [Dark] returns [Light], [Light] returns [Dark],
     *         and [System] returns the opposite of the current system theme.
     */
    abstract fun opposite(isSystemInDarkTheme: Boolean): Theme

    /**
     * The unique ordinal identifier for this theme mode.
     *
     * Used for serialization and persistence. Each theme has a distinct ordinal value:
     * - [Light]: 0
     * - [Dark]: 1  
     * - [System]: 2
     */
    abstract val ordinal: Int

    /**
     * Light theme mode that forces the application to use light colors.
     */
    object Light : Theme() {
        override fun opposite(isSystemInDarkTheme: Boolean): Theme = Dark
        override val ordinal: Int = 0
    }

    /**
     * Dark theme mode that forces the application to use dark colors.
     */
    object Dark : Theme() {
        override fun opposite(isSystemInDarkTheme: Boolean): Theme = Light
        override val ordinal: Int = 1
    }

    /**
     * System theme mode that follows the system's theme preference.
     *
     * When in system mode, the application adapts to the user's device-wide
     * theme setting (light or dark). The [opposite] function returns the
     * theme that would be opposite to the current system preference.
     */
    object System : Theme() {
        override fun opposite(isSystemInDarkTheme: Boolean): Theme {
            return if (isSystemInDarkTheme) {
                Light
            } else {
                Dark
            }
        }

        override val ordinal: Int = 2
    }

    companion object {
        /**
         * Creates a [Theme] instance from its ordinal value.
         *
         * This is the inverse operation of [ordinal], used for deserialization
         * and restoration from persistent storage.
         *
         * @param ordinal The ordinal value representing the theme:
         *                - 0 for [Light]
         *                - 1 for [Dark]
         *                - 2 for [System]
         * @return The corresponding [Theme] instance.
         * @throws IllegalArgumentException if the ordinal value is not valid.
         */
        fun fromOrdinal(ordinal: Int): Theme {
            return when (ordinal) {
                Light.ordinal -> Light
                Dark.ordinal -> Dark
                System.ordinal -> System
                else -> throw IllegalArgumentException("Invalid theme ordinal")
            }
        }
    }
}