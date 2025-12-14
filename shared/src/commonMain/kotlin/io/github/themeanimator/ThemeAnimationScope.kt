package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer

@Composable
fun <T> ThemeAnimationScope(
    theme: T,
    content: @Composable () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier.themeAnimation(theme, graphicsLayer)
    ) {
        content()
    }
}