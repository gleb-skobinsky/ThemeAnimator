package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer

@Composable
fun <T> ThemeAnimationScope(
    state: ThemeAnimationState,
    theme: T,
    content: @Composable () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier.themeAnimation(
            state = state,
            theme = theme,
            graphicsLayer = graphicsLayer
        )
    ) {
        content()
    }
}