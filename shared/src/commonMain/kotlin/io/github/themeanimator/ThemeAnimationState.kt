package io.github.themeanimator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * A state holder that manages theme toggle animations.
 *
 * This class provides internal state management for theme animations, including the current
 * theme state, button position tracking, and recording coordination for smooth theme transitions.
 * It is not recommended to use the class constructor directly. Use the [rememberThemeAnimationState] method instead.
 *
 * @param initialIsDark The initial theme state. `true` for dark theme, `false` for light theme.
 * @param coroutineScope The coroutine scope used for launching asynchronous operations,
 * such as record request coordination.
 * @param animationSpec The animation specification for theme transitions.
 * @param format The format/style of the theme animation. See [ThemeAnimationFormat] for available styles.
 * @param useDynamicContent Whether to capture and animate dynamic content during theme transitions.
 */
@Stable
class ThemeAnimationState(
    initialIsDark: Boolean,
    private val coroutineScope: CoroutineScope,
    internal val animationSpec: AnimationSpec<Float>,
    internal val format: ThemeAnimationFormat,
    internal val useDynamicContent: Boolean,
) {
    var isDark: Boolean by mutableStateOf(initialIsDark)
        private set

    internal val requestRecord = MutableStateFlow(RecordStatus.Initial)

    internal var buttonPosition: Offset? by mutableStateOf(null)
        private set

    fun updateButtonPosition(position: Rect) {
        buttonPosition = position.center
    }

    fun toggleTheme() {
        coroutineScope.launch {
            requestRecord.value = RecordStatus.RecordRequested
            requestRecord.firstOrNull { it == RecordStatus.Recorded }
            isDark = !isDark
        }
    }
}

/**
 * Creates a state holder for theme toggle animations with customizable
 * animation characteristics and behavior. The state is remembered across recompositions.
 *
 * @param animationSpec the animation specification for the theme transition.
 * Defaults to a 300ms tween animation.
 * @param isDark the initial theme state. `true` for dark theme, `false` for light theme.
 * @param format the format/style of the theme animation.
 * Defaults to [ThemeAnimationFormat.Sliding]. For more styles, see [ThemeAnimationFormat].
 * @param useDynamicContent if set to `true`, the target content (that is, the theme
 * to which the theme is toggled) will be drawn dynamically and any changes to it, including scrolling,
 * animations etc. will be preserved. Note that the abandoned theme will still be drawn statically, leading to inconsistency
 * between the target and abandoned parts of the animation. Defaults to `false`.
 *
 * @return A remembered [ThemeAnimationState] instance that can be used to control
 * theme animations within a Composable.
 */
@Composable
fun rememberThemeAnimationState(
    animationSpec: AnimationSpec<Float> = tween(300),
    isDark: Boolean = isSystemInDarkTheme(),
    format: ThemeAnimationFormat = ThemeAnimationFormat.Sliding,
    useDynamicContent: Boolean = false,
): ThemeAnimationState {
    val coroutineScope = rememberCoroutineScope()
    return remember(
        coroutineScope,
        animationSpec,
        useDynamicContent,
        format
    ) {
        ThemeAnimationState(
            initialIsDark = isDark,
            coroutineScope = coroutineScope,
            animationSpec = animationSpec,
            format = format,
            useDynamicContent = useDynamicContent
        )
    }
}