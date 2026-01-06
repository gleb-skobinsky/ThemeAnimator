package io.github.themeanimator.theme

import androidx.compose.runtime.Immutable

@Immutable
sealed class Theme {
    abstract fun opposite(isSystemInDarkTheme: Boolean): Theme

    internal abstract val ordinal: Int

    object Light : Theme() {
        override fun opposite(isSystemInDarkTheme: Boolean): Theme = Dark
        override val ordinal: Int = 0
    }
    object Dark : Theme() {
        override fun opposite(isSystemInDarkTheme: Boolean): Theme = Light
        override val ordinal: Int = 1
    }
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
        internal fun fromOrdinal(ordinal: Int): Theme {
            return when (ordinal) {
                Light.ordinal -> Light
                Dark.ordinal -> Dark
                System.ordinal -> System
                else -> throw IllegalArgumentException("Invalid theme ordinal")
            }
        }
    }
}