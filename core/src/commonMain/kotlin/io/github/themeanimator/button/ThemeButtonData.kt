package io.github.themeanimator.button

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
internal sealed interface ThemeButtonData {
    val switchMode: ButtonSwitchMode

    sealed interface Constant<Value : Any> : ThemeButtonData {
        val icon: Value

        override val switchMode: ButtonSwitchMode
            get() = ButtonSwitchMode.Binary
    }

    sealed interface Binary<ValueLight : Any, ValueDark : Any> : ThemeButtonData {
        val iconLight: ValueLight
        val iconDark: ValueDark
        override val switchMode: ButtonSwitchMode
            get() = ButtonSwitchMode.Binary
    }

    sealed interface TriState<ValueLight : Any, ValueDark : Any, ValueSystem : Any> :
        ThemeButtonData {
        val iconLight: ValueLight
        val iconDark: ValueDark
        val iconSystem: ValueSystem
        override val switchMode: ButtonSwitchMode
            get() = ButtonSwitchMode.TriState
    }

    sealed interface Animatable : ThemeButtonData {
        val animationSpec: AnimationSpec<Float>
        val lightProgress: Float
        val darkProgress: Float
    }

    sealed interface BinaryAnimatable : Animatable {
        override val switchMode: ButtonSwitchMode
            get() = ButtonSwitchMode.Binary
    }

    sealed interface TriStateAnimatable : Animatable {
        val systemProgress: Float
        override val switchMode: ButtonSwitchMode
            get() = ButtonSwitchMode.TriState
    }

    data class SomeVector(
        override val icon: ImageVector,
    ) : Constant<ImageVector>

    data class SomePainter(
        override val icon: Painter,
    ) : Constant<Painter>

    data class DuoVector(
        override val iconDark: ImageVector,
        override val iconLight: ImageVector,
    ) : Binary<ImageVector, ImageVector>

    data class DuoPainter(
        override val iconLight: Painter,
        override val iconDark: Painter,
    ) : Binary<Painter, Painter>

    data class TriStateVector(
        override val iconDark: ImageVector,
        override val iconLight: ImageVector,
        override val iconSystem: ImageVector,
    ) : TriState<ImageVector, ImageVector, ImageVector>

    data class TriStatePainter(
        override val iconDark: Painter,
        override val iconLight: Painter,
        override val iconSystem: Painter,
    ) : TriState<Painter, Painter, Painter>

    data class DuoLottie(
        override val animationSpec: AnimationSpec<Float>,
        override val darkProgress: Float,
        override val lightProgress: Float,
    ) : BinaryAnimatable

    data class TriStateLottie(
        override val animationSpec: AnimationSpec<Float>,
        override val darkProgress: Float,
        override val lightProgress: Float,
        override val systemProgress: Float,
    ) : TriStateAnimatable
}