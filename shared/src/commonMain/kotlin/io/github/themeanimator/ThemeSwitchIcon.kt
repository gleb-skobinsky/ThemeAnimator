package io.github.themeanimator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieCompositionSpec.Companion.JsonString
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlin.jvm.JvmInline

@Immutable
sealed interface ThemeSwitchIcon {

    @Composable
    fun Icon(
        state: ThemeAnimationState,
        tint: Color,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
    )

    @JvmInline
    value class Vector(
        val imageVector: ImageVector,
    ) : ThemeSwitchIcon {

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    data class DuoVector(
        val darkVector: ImageVector,
        val lightVector: ImageVector,
    ) : ThemeSwitchIcon {

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                imageVector = if (state.isDark) darkVector else lightVector,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    @JvmInline
    value class RasterPainter(
        val painter: Painter,
    ) : ThemeSwitchIcon {

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    data class DuoRasterPainter(
        val darkPainter: Painter,
        val lightPainter: Painter,
    ) : ThemeSwitchIcon {

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                painter = if (state.isDark) darkPainter else lightPainter,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    @ConsistentCopyVisibility
    data class LottieFilePainter internal constructor(
        val animationSpec: AnimationSpec<Float>,
        val startProgress: Float,
        val endProgress: Float,
        val onReadContent: suspend () -> LottieCompositionSpec,
    ) : ThemeSwitchIcon {

        internal constructor(
            animationSpec: AnimationSpec<Float>,
            startProgress: Float,
            endProgress: Float,
            onReadContentJson: suspend () -> String,
        ) : this(
            animationSpec = animationSpec,
            startProgress = startProgress,
            endProgress = endProgress,
            onReadContent = { JsonString(onReadContentJson()) }
        )

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            val progress by animateLottieProgress(
                state = state,
                animationSpec = animationSpec,
                startProgress = startProgress,
                endProgress = endProgress
            )
            val composition by rememberLottieComposition(
                spec = onReadContent
            )
            Icon(
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }
}

@Composable
fun rememberLottieIcon(
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
        onReadContentJson = onReadContentJson,
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
private fun animateLottieProgress(
    state: ThemeAnimationState,
    animationSpec: AnimationSpec<Float>,
    startProgress: Float,
    endProgress: Float,
): State<Float> = animateFloatAsState(
    targetValue = if (state.isDark) startProgress else endProgress,
    animationSpec = animationSpec
)