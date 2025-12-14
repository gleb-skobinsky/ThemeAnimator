package io.github.themeanimator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeAnimationState<T>(
    private val coroutineScope: CoroutineScope,
    initialTheme: T,
) {
    private val _currentTheme = MutableStateFlow(initialTheme)
    val currentTheme = _currentTheme.asStateFlow()

    internal fun updateCurrentTheme(newTheme: T) {
        _currentTheme.value = newTheme
    }
}

@Composable
fun <T> rememberThemeAnimationState(
    initialTheme: T,
): ThemeAnimationState<T> {
    val scope = rememberCoroutineScope()
    return remember(
        initialTheme
    ) {
        ThemeAnimationState(
            coroutineScope = scope,
            initialTheme = initialTheme
        )
    }
}