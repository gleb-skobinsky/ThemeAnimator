package io.github.themeanimator.theme

import kotlinx.coroutines.flow.StateFlow

interface ThemeProvider {
    val currentTheme: StateFlow<Theme>

    suspend fun updateTheme(theme: Theme)
}