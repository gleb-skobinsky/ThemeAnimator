package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer

/**
 * A composable scope that enables theme animations for its content.
 *
 * The animation captures the visual state of the content before and after the theme change,
 * creating a smooth transition effect. Place this scope around the composable tree that
 * should be animated during theme toggles.
 *
 * @param state The [ThemeAnimationState] instance that controls the theme animation.
 *              This state manages the current theme, animation timing, and format.
 * @param theme The current theme value. Changes to this value trigger the theme animation.
 *              Typically, this would be a theme object or boolean indicating the theme state.
 * @param content The composable content to be displayed and animated during theme transitions.
 *                This content will be captured and rendered with theme animation effects.
 */
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