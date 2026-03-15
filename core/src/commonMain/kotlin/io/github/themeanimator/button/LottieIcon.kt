package io.github.themeanimator.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieCompositionSpec.Companion.JsonString
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.theme.Theme
import io.github.themeanimator.theme.isDark

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a JSON string.
 *
 * This is a convenience function for [rememberLottieIcon] when the Lottie source is a JSON string.
 * For other composition sources (files, URLs, etc.), use [rememberLottieIcon] directly.
 *
 * @param animationSpec The animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param darkThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
 * @param lightThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
 * @param onReadContent A suspend function that provides the Lottie animation JSON string.
 *                          This is called when the icon is first composed and may perform I/O operations
 *                          to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIcon(
    lightThemeProgress: Float,
    darkThemeProgress: Float,
    animationSpec: AnimationSpec<Float>,
    onReadContent: suspend () -> LottieCompositionSpec,
): ThemeSwitchIcon.LottieFilePainter {
    return remember(
        onReadContent,
        lightThemeProgress,
        darkThemeProgress,
        animationSpec
    ) {
        val buttonData = ThemeButtonData.DuoLottie(
            darkProgress = darkThemeProgress,
            lightProgress = lightThemeProgress,
            animationSpec = animationSpec
        )
        ThemeSwitchIcon.LottieFilePainter(
            data = buttonData,
            onReadContent = onReadContent,
        )
    }
}

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a JSON string.
 *
 * This is a convenience function for [rememberLottieIcon] when the Lottie source is a JSON string.
 * For other composition sources (files, URLs, etc.), use [rememberLottieIcon] directly.
 *
 * @param animationSpec The animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param darkThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
 * @param lightThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
 * @param systemThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in system theme.
 * @param onReadContent A suspend function that provides the Lottie animation JSON string.
 *                          This is called when the icon is first composed and may perform I/O operations
 *                          to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIcon(
    lightThemeProgress: Float,
    darkThemeProgress: Float,
    systemThemeProgress: Float,
    animationSpec: AnimationSpec<Float>,
    onReadContent: suspend () -> LottieCompositionSpec,
): ThemeSwitchIcon.LottieFilePainter {
    return remember(
        onReadContent,
        lightThemeProgress,
        darkThemeProgress,
        systemThemeProgress,
        animationSpec
    ) {
        ThemeSwitchIcon.LottieFilePainter(
            data = ThemeButtonData.TriStateLottie(
                darkProgress = darkThemeProgress,
                lightProgress = lightThemeProgress,
                systemProgress = systemThemeProgress,
                animationSpec = animationSpec
            ),
            onReadContent = onReadContent,
        )
    }
}

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a composition specification.
 *
 * This function supports all Lottie composition specification types.
 *
 * @param animationSpec The animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param darkThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
 * @param lightThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
 * @param onReadContentJson A suspend function that provides the [LottieCompositionSpec] defining
 *                      the animation source. This is called when the icon is first composed and may
 *                      perform I/O operations to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIconJson(
    lightThemeProgress: Float,
    darkThemeProgress: Float,
    animationSpec: AnimationSpec<Float>,
    onReadContentJson: suspend () -> String,
) = rememberLottieIcon(
    lightThemeProgress = lightThemeProgress,
    darkThemeProgress = darkThemeProgress,
    animationSpec = animationSpec
) {
    JsonString(onReadContentJson())
}

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a composition specification.
 *
 * This function supports all Lottie composition specification types.
 *
 * @param animationSpec The animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param darkThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
 * @param lightThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
 * @param systemThemeProgress The Lottie animation progress value (0.0f to 1.0f) to display when in system theme.
 * @param onReadContentJson A suspend function that provides the [LottieCompositionSpec] defining
 *                      the animation source. This is called when the icon is first composed and may
 *                      perform I/O operations to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIconJson(
    lightThemeProgress: Float,
    darkThemeProgress: Float,
    systemThemeProgress: Float,
    animationSpec: AnimationSpec<Float>,
    onReadContentJson: suspend () -> String,
) = rememberLottieIcon(
    lightThemeProgress = lightThemeProgress,
    darkThemeProgress = darkThemeProgress,
    systemThemeProgress = systemThemeProgress,
    animationSpec = animationSpec
) {
    JsonString(onReadContentJson())
}

@Composable
internal fun animateLottieProgress(
    state: ThemeAnimationState,
    buttonData: ThemeButtonData.Animatable,
): State<Float> {
    return when (buttonData) {
        is ThemeButtonData.BinaryAnimatable -> {
            animateFloatAsState(
                targetValue = if (state.uiTheme.isDark()) {
                    buttonData.lightProgress
                } else {
                    buttonData.darkProgress
                },
                animationSpec = buttonData.animationSpec
            )
        }

        is ThemeButtonData.TriStateAnimatable -> {
            animateFloatAsState(
                targetValue = when (state.uiTheme) {
                    Theme.Light -> buttonData.lightProgress
                    Theme.Dark -> buttonData.darkProgress
                    Theme.System -> buttonData.systemProgress
                },
                animationSpec = buttonData.animationSpec
            )
        }
    }
}