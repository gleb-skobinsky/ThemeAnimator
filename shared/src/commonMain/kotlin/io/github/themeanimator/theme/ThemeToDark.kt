package io.github.themeanimator.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun Theme.isDark(): Boolean = when (this) {
    Theme.Light -> false
    Theme.Dark -> true
    Theme.System if isSystemInDarkTheme() -> true
    else -> false
}