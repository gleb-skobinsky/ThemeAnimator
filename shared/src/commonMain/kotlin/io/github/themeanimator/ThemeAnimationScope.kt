package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.ui.Modifier

@Composable
fun <T> ThemeAnimationScope(
    currentTheme: T,
    initialTheme: T,
    themeProvider: CompositionLocal<T>,
    content: @Composable () -> Unit,
) {
    val animationState = rememberThemeAnimationState(
        initialTheme = initialTheme
    )
    Box(Modifier.themeAnimation(animationState)) {
        content()
    }
}