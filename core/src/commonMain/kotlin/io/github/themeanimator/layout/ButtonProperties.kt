package io.github.themeanimator.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.button.ThemeSwitchIcon

@Immutable
@PublishedApi
internal data class ButtonProperties(
    val type: ButtonSwitchType,
    val animationState: ThemeAnimationState,
    val icon: ThemeSwitchIcon,
    val modifier: Modifier,
    val iconSize: DpSize,
    val iconScale: Float,
    val iconTint: Color,
    val iconShape: Shape = RoundedCornerShape(50)
)