package io.github.themeanimator

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.GraphicsLayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

class ThemeAnimationState<T>(
    internal val graphicsLayer: GraphicsLayer,
    coroutineScope: CoroutineScope,
    initialTheme: T,
) {
    private val _currentTheme = MutableStateFlow(initialTheme)
    val currentTheme = _currentTheme.asStateFlow()

    private val _animationProgress = MutableStateFlow(0f)
    val animationProgress = _animationProgress.asStateFlow()

    private val _isAnimating = MutableStateFlow(false)
    val isAnimating = _isAnimating.asStateFlow()

    internal fun updateCurrentTheme(newTheme: T) {
        _currentTheme.value = newTheme
    }

    var prevImageBitmap: ImageBitmap? = null
        private set
    var currentImageBitmap: ImageBitmap? = null
        private set

    init {
        coroutineScope.launch {
            currentTheme.collectLatest {
                _isAnimating.value = true
                prevImageBitmap = currentImageBitmap
                currentImageBitmap = graphicsLayer.toImageBitmap()
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = tween(3000)
                ) { value, _ ->
                    _animationProgress.value = value
                }
                _isAnimating.value = false
            }
        }
    }
}

@Composable
fun <T> rememberThemeAnimationState(
    theme: T,
    graphicsLayer: GraphicsLayer,
): ThemeAnimationState<T> {
    val coroutineScope = rememberCoroutineScope()
    return remember(
        graphicsLayer,
        coroutineScope
    ) {
        ThemeAnimationState(
            graphicsLayer = graphicsLayer,
            initialTheme = theme,
            coroutineScope = coroutineScope
        )
    }
}