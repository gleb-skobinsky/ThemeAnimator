@file:Suppress("Unused")

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
import io.github.themeanimator.theme.isDark
import kotlin.jvm.JvmInline

/**
 * Defines theme-aware icons for the theme switch button.
 *
 * Implementations of this interface provide icons that automatically adapt to the current
 * theme state. Different implementations support various icon sources including vector graphics,
 * raster images, and Lottie animations.
 */
@Immutable
sealed interface ThemeSwitchIcon<ButtonData : ThemeButtonData> {
    val data: ButtonData

    /**
     * Renders the icon based on the current theme state.
     *
     * @param state The [ThemeAnimationState] containing the current theme information.
     *              The [ThemeAnimationState.uiTheme] property determines which icon variant
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
     */
    @Immutable
    @JvmInline
    value class Vector(
        override val data: ThemeButtonData.SomeVector,
    ) : ThemeSwitchIcon<ThemeButtonData.SomeVector> {
        constructor(vector: ImageVector) : this(ThemeButtonData.SomeVector(vector))

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    /**
     * A pair of vector icons that switch based on the current theme.
     */
    @Immutable
    data class DuoVector(
        override val data: ThemeButtonData.DuoVector,
    ) : ThemeSwitchIcon<ThemeButtonData.DuoVector> {
        constructor(
            darkVector: ImageVector,
            lightVector: ImageVector,
        ) : this(ThemeButtonData.DuoVector(darkVector, lightVector))

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                imageVector = if (state.uiTheme.isDark()) {
                    data.iconDark
                } else {
                    data.iconLight
                },
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    /**
     * A single raster painter that remains constant regardless of theme state.
     */
    @JvmInline
    @Immutable
    value class RasterPainter(
        override val data: ThemeButtonData.SomePainter,
    ) : ThemeSwitchIcon<ThemeButtonData.SomePainter> {
        constructor(painter: Painter) : this(ThemeButtonData.SomePainter(painter))

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                painter = data.icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    /**
     * A pair of raster painters that switch based on the current theme.
     */
    @Immutable
    data class DuoRasterPainter(
        override val data: ThemeButtonData.DuoPainter,
    ) : ThemeSwitchIcon<ThemeButtonData.DuoPainter> {
        /**
         * This implementation displays [darkPainter] when in dark theme and [lightPainter]
         * when in light theme. The icon automatically transitions when the theme changes.
         */
        constructor(
            darkPainter: Painter,
            lightPainter: Painter,
        ) : this(
            ThemeButtonData.DuoPainter(
                iconLight = lightPainter,
                iconDark = darkPainter
            )
        )

        @Composable
        override fun Icon(
            state: ThemeAnimationState,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                painter = if (state.uiTheme.isDark()) {
                    data.iconDark
                } else {
                    data.iconLight
                },
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    /**
     * A Lottie animation icon that animates based on the current theme state.
     */
    @ConsistentCopyVisibility
    data class LottieFilePainter internal constructor(
        override val data: ThemeButtonData.Animatable,
        val onReadContent: suspend () -> LottieCompositionSpec,
    ) : ThemeSwitchIcon<ThemeButtonData.Animatable> {

        /**
         * This implementation displays a Lottie animation with its progress animated between
         * [darkProgress] and [lightProgress] based on the theme state. The animation smoothly
         * transitions when the theme changes using the specified [animationSpec].
         *
         * The Lottie composition is loaded asynchronously using [onReadContent], which returns
         * a [LottieCompositionSpec] that defines the animation source (Lottie JSON, DotLottie file, etc.).
         *
         * @param animationSpec The animation specification controlling the interpolation between
         *                      progress values during theme transitions.
         * @param darkProgress The Lottie animation progress value (0.0f to 1.0f) to display when in dark theme.
         * @param lightProgress The Lottie animation progress value (0.0f to 1.0f) to display when in light theme.
         * @param onReadContent A suspend function that provides the Lottie composition specification.
         *                      This is called when the icon is first composed and may perform I/O operations
         *                      to load the animation source.
         */
        constructor(
            animationSpec: AnimationSpec<Float>,
            darkProgress: Float,
            lightProgress: Float,
            onReadContent: suspend () -> LottieCompositionSpec,
        ) : this(
            data = ThemeButtonData.DuoLottie(
                animationSpec = animationSpec,
                darkProgress = darkProgress,
                lightProgress = lightProgress
            ),
            onReadContent = onReadContent
        )

        constructor(
            animationSpec: AnimationSpec<Float>,
            darkProgress: Float,
            lightProgress: Float,
            systemProgress: Float,
            onReadContent: suspend () -> LottieCompositionSpec,
        ) : this(
            data = ThemeButtonData.TriStateLottie(
                animationSpec = animationSpec,
                darkProgress = darkProgress,
                lightProgress = lightProgress,
                systemProgress = systemProgress,
            ),
            onReadContent = onReadContent
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
                buttonData = data
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
