package io.github.themeanimator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import io.github.themeanimator.theme.Theme
import io.github.themeanimator.theme.ThemeProvider
import io.github.themeanimator.theme.rememberRuntimeThemeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * A state holder that manages theme toggle animations.
 *
 * This class provides internal state management for theme animations, including the current
 * theme state, button position tracking, and recording coordination for smooth theme transitions.
 * It is not recommended to use the class constructor directly. Use the [rememberThemeAnimationState] method instead.
 *
 * @param themeProvider A [ThemeProvider] instance that provides the current theme and allows for theme updates. Typically,
 * a data access object that saves the theme to a local or remote storage.
 * @param coroutineScope The coroutine scope used for launching asynchronous operations,
 * such as record request coordination.
 * @param animationSpec The animation specification for theme transitions.
 * @param format The format/style of the theme animation. See [ThemeAnimationFormat] for available styles.
 * @param useDynamicContent Whether to capture and animate dynamic content during theme transitions. If set to `true`, the target content (that is, the theme
 *  * to which the theme is toggled) will be drawn dynamically and any changes to it, including scrolling,
 *  * animations etc. will be preserved. Note that the abandoned theme will still be drawn statically, which may lead to inconsistency
 *  * between the target and abandoned parts of the animation. Defaults to `false`.
 */
@Stable
class ThemeAnimationState(
    private val themeProvider: ThemeProvider,
    private val coroutineScope: CoroutineScope,
    internal val animationSpec: AnimationSpec<Float>,
    internal val format: ThemeAnimationFormat,
    internal val useDynamicContent: Boolean,
) {

    /**
     * The current theme being displayed in the UI.
     *
     * This property reflects the theme currently shown to the user and may differ
     * temporarily from [ThemeProvider.currentTheme] during animation sequences.
     * It's automatically updated when theme changes are received from the provider.
     */
    var uiTheme: Theme by mutableStateOf(themeProvider.currentTheme.value)
        private set

    internal val requestRecord = MutableStateFlow(RecordStatus.Initial)

    internal var buttonPosition: Offset? by mutableStateOf(null)
        private set

    /**
     * Updates the button position using a rectangular bounds.
     *
     * This method sets the button position to the center of the provided rectangle.
     * The position is used by animation formats that require knowledge of the
     * interaction point for effects like circular reveals.
     *
     * @param position The rectangular bounds of the theme toggle button.
     */
    fun updateButtonPosition(position: Rect) {
        updateButtonPosition(position.center)
    }

    /**
     * Updates the button position using screen coordinates.
     *
     * This method directly sets the button position to the specified point.
     * The position is used by animation formats that require knowledge of the
     * interaction point for effects like circular reveals.
     *
     * @param position The screen coordinates of the theme toggle button.
     */
    fun updateButtonPosition(position: Offset) {
        buttonPosition = position
    }

    private var toggleThemeJob: Job? = null

    /**
     * Toggles the current theme to its opposite value.
     *
     * This method initiates a theme change by determining the opposite of the current
     * theme and updating the theme provider. The method prevents multiple simultaneous
     * toggle operations and coordinates with the animation system for smooth transitions.
     *
     * @param isSystemInDarkTheme Whether the system is currently in dark theme mode.
     *                           This parameter is used to determine the opposite theme
     *                           when the current theme is [Theme.System].
     */
    fun toggleTheme(isSystemInDarkTheme: Boolean) {
        if (toggleThemeJob?.isActive == true) return
        toggleThemeJob = coroutineScope.launch {
            themeProvider.updateTheme(
                themeProvider.currentTheme.value.opposite(isSystemInDarkTheme)
            )
        }
    }

    init {
        coroutineScope.launch {
            themeProvider.currentTheme
                // Skip the initial theme
                .drop(1)
                .collectLatest { newTheme ->
                    requestRecord.value = RecordStatus.RecordRequested
                    requestRecord.firstOrNull { it == RecordStatus.Recorded }
                    uiTheme = newTheme
                    requestRecord.value = RecordStatus.PrepareForAnimation
                }
        }
    }
}

/**
 * Creates a state holder for theme toggle animations with customizable
 * animation characteristics and behavior. The state is remembered across recompositions.
 *
 * @param animationSpec the animation specification for the theme transition.
 * Defaults to a 300ms tween animation.
 * @param format the format/style of the theme animation.
 * Defaults to [ThemeAnimationFormat.Sliding]. For more styles, see [ThemeAnimationFormat].
 * @param useDynamicContent Whether to capture and animate dynamic content during theme transitions.
 * If set to `true`, the target content (that is, the theme
 * to which the theme is toggled) will be drawn dynamically and any changes to it, including scrolling,
 * animations etc. will be preserved. Note that the abandoned theme will still be drawn statically, which may lead to inconsistency
 * between the target and abandoned parts of the animation. Defaults to `false`.
 *
 * @return A remembered [ThemeAnimationState] instance that can be used to control
 * theme animations within a Composable.
 */
@Composable
fun rememberThemeAnimationState(
    themeProvider: ThemeProvider = rememberRuntimeThemeProvider(),
    animationSpec: AnimationSpec<Float> = tween(300),
    format: ThemeAnimationFormat = ThemeAnimationFormat.Sliding,
    useDynamicContent: Boolean = false,
): ThemeAnimationState {
    val coroutineScope = rememberCoroutineScope()
    return remember(
        themeProvider,
        coroutineScope,
        animationSpec,
        useDynamicContent,
        format
    ) {
        ThemeAnimationState(
            themeProvider = themeProvider,
            coroutineScope = coroutineScope,
            animationSpec = animationSpec,
            format = format,
            useDynamicContent = useDynamicContent
        )
    }
}