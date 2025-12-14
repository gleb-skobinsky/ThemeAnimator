package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer

@Composable
fun <T> ThemeAnimationScope(
    theme: T,
    content: @Composable () -> Unit,
) {
    val graphicsLayer = rememberGraphicsLayer()
    val animationState = rememberThemeAnimationState(
        theme = theme,
        graphicsLayer = graphicsLayer
    )

    LaunchedEffect(theme) {
        animationState.updateCurrentTheme(theme)
    }

    Box(
        modifier = Modifier.drawWithContent {
            graphicsLayer.record {
                this@drawWithContent.drawContent()
            }
            drawLayer(graphicsLayer)
        }.themeAnimation(animationState, content)
    ) {
        content()
    }
}