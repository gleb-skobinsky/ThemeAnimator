package io.github.themeanimator.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieCompositionSpec.Companion.JsonString
import io.github.themeanimator.ThemeAnimationState

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a JSON string.
 *
 * This function creates a Lottie icon that animates between [startProgress] and [endProgress]
 * based on the theme state. The Lottie composition is loaded from a JSON string provided by
 * [onReadContentJson].
 *
 * This is a convenience function for [rememberLottieIcon] when the Lottie source is a JSON string.
 * For other composition sources (files, URLs, etc.), use [rememberLottieIcon] directly.
 *
 * @param animationSpec The animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param startProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
 * @param endProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
 * @param onReadContentJson A suspend function that provides the Lottie animation JSON string.
 *                          This is called when the icon is first composed and may perform I/O operations
 *                          to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIconJson(
    animationSpec: AnimationSpec<Float>,
    startProgress: Float,
    endProgress: Float,
    onReadContentJson: suspend () -> String,
) = remember(
    onReadContentJson,
    startProgress,
    endProgress,
    animationSpec
) {
    ThemeSwitchIcon.LottieFilePainter(
        startProgress = startProgress,
        endProgress = endProgress,
        onReadContent = { JsonString(onReadContentJson()) },
        animationSpec = animationSpec,
    )
}

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a composition specification.
 *
 * This function creates a Lottie icon that animates between [startProgress] and [endProgress]
 * based on the theme state. The Lottie composition is loaded from the specification provided
 * by [onReadContent].
 *
 * This function supports all Lottie composition specification types.
 *
 * @param animationSpec The animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param startProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
 * @param endProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
 * @param onReadContent A suspend function that provides the [LottieCompositionSpec] defining
 *                      the animation source. This is called when the icon is first composed and may
 *                      perform I/O operations to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIcon(
    animationSpec: AnimationSpec<Float>,
    startProgress: Float,
    endProgress: Float,
    onReadContent: suspend () -> LottieCompositionSpec,
) = remember(
    onReadContent,
    startProgress,
    endProgress,
    animationSpec
) {
    ThemeSwitchIcon.LottieFilePainter(
        startProgress = startProgress,
        endProgress = endProgress,
        onReadContent = onReadContent,
        animationSpec = animationSpec,
    )
}

@Composable
internal fun animateLottieProgress(
    state: ThemeAnimationState,
    animationSpec: AnimationSpec<Float>,
    startProgress: Float,
    endProgress: Float,
): State<Float> = animateFloatAsState(
    targetValue = if (state.isDark) startProgress else endProgress,
    animationSpec = animationSpec
)