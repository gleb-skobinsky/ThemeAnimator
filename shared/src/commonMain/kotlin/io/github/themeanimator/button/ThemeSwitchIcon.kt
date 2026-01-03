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

/**
 * Defines theme-aware icons for the theme switch button.
 *
 * Implementations of this interface provide icons that automatically adapt to the current
 * theme state. Different implementations support various icon sources including vector graphics,
 * raster images, and Lottie animations.
 */
@Immutable
sealed interface ThemeSwitchIcon {

    /**
     * Renders the icon based on the current theme state.
     *
     * @param state The [ThemeAnimationState] containing the current theme information.
     *              The [ThemeAnimationState.isDark] property determines which icon variant
     *              to display for implementations with multiple icons.
     * @param tint The color tint to apply to the icon. Provide `Color.Unspecified` to apply no tint.
     * @param modifier The modifier to be applied to the icon component.
     * @param contentDescription The accessibility description for the icon. This should describe
     *                          the icon's purpose for screen readers.
     */
    @Composable
    fun Icon(
        state: ThemeAnimationState,
        tint: Color,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
    )

    /**
     * A single vector icon that remains constant regardless of theme state.
     *
     * This implementation displays the same [imageVector] in both light and dark themes.
     * Use this when you want the icon to remain unchanged during theme transitions.
     */
    @Immutable
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

    /**
     * A pair of vector icons that switch based on the current theme.
     *
     * This implementation displays [darkVector] when in dark theme and [lightVector]
     * when in light theme. The icon automatically transitions when the theme changes.
     */
    @Immutable
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

    /**
     * A single raster painter that remains constant regardless of theme state.
     *
     * This implementation displays the same [painter] in both light and dark themes.
     * Use this when you want the raster icon to remain unchanged during theme transitions.
     */
    @JvmInline
    @Immutable
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

    /**
     * A pair of raster painters that switch based on the current theme.
     *
     * This implementation displays [darkPainter] when in dark theme and [lightPainter]
     * when in light theme. The icon automatically transitions when the theme changes.
     */
    @Immutable
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

    /**
     * A Lottie animation icon that animates based on the current theme state.
     *
     * This implementation displays a Lottie animation with its progress animated between
     * [startProgress] and [endProgress] based on the theme state. The animation smoothly
     * transitions when the theme changes using the specified [animationSpec].
     *
     * The Lottie composition is loaded asynchronously using [onReadContent], which returns
     * a [LottieCompositionSpec] that defines the animation source (Lottie JSON, DotLottie file, etc.).
     *
     * @param animationSpec The animation specification controlling the interpolation between
     *                      progress values during theme transitions.
     * @param startProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
     * @param endProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
     * @param onReadContent A suspend function that provides the Lottie composition specification.
     *                      This is called when the icon is first composed and may perform I/O operations
     *                      to load the animation source.
     */
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
