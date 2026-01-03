package io.github.themeanimator.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieCompositionSpec.Companion.JsonString
import io.github.themeanimator.ThemeAnimationState

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