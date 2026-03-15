package io.github.themeanimator.button

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
 * @param buttonData The Lottie animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param onReadContentJson A suspend function that provides the Lottie animation JSON string.
 *                          This is called when the icon is first composed and may perform I/O operations
 *                          to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIconJson(
    buttonData: ThemeButtonData.Animatable,
    onReadContentJson: suspend () -> String,
) = remember(
    onReadContentJson,
    buttonData
) {
    ThemeSwitchIcon.LottieFilePainter(
        data = buttonData,
        onReadContent = { JsonString(onReadContentJson()) },
    )
}

/**
 * Remembers a [ThemeSwitchIcon.LottieFilePainter] that loads Lottie animation from a composition specification.
 *
 * This function supports all Lottie composition specification types.
 *
 * @param buttonData The Lottie animation specification controlling the interpolation between
 *                      progress values during theme transitions.
 * @param onReadContent A suspend function that provides the [LottieCompositionSpec] defining
 *                      the animation source. This is called when the icon is first composed and may
 *                      perform I/O operations to load the animation source.
 * @return A remembered [ThemeSwitchIcon.LottieFilePainter] that can be used with [ThemeSwitchButton].
 */
@Composable
fun rememberLottieIcon(
    buttonData: ThemeButtonData.Animatable,
    onReadContent: suspend () -> LottieCompositionSpec,
) = remember(
    onReadContent,
    buttonData
) {
    ThemeSwitchIcon.LottieFilePainter(
        data = buttonData,
        onReadContent = onReadContent,
    )
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