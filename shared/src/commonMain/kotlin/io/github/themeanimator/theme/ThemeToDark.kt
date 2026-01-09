package io.github.themeanimator.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * Determines if this theme resolves to a dark appearance.
 *
 * This extension function evaluates the theme's effective appearance based on the
 * theme type and current system settings:
 * - [Theme.Light] always returns `false`
 * - [Theme.Dark] always returns `true`
 * - [Theme.System] returns `true` if the system is in dark mode, `false` otherwise
 *
 * @return `true` if the theme should display dark colors, `false` for light colors.
 */
@Composable
fun Theme.isDark(): Boolean = when (this) {
    Theme.Light -> false
    Theme.Dark -> true
    Theme.System if isSystemInDarkTheme() -> true
    else -> false
}