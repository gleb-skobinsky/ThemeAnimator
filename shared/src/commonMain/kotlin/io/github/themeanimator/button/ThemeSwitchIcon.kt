package io.github.themeanimator.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.github.themeanimator.ThemeAnimationState
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

    @Immutable
    data class Vector(
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
