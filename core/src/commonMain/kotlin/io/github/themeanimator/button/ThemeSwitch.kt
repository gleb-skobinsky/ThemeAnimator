package io.github.themeanimator.button

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.layout.ButtonSwitchType
import io.github.themeanimator.layout.ThemeAnimationLayoutScope

/**
 * A composable abstract switch that toggles themes in a binary or tristate transition.
 *
 * @param animationState The [ThemeAnimationState] that controls the theme switch animation.
 *                       This state tracks the current theme and manages the animation lifecycle.
 * @param buttonIcon The icon to display on the switch. Defaults to [DefaultButtonIcon] which
 *                   shows a sun icon for dark theme and moon icon for light theme. Custom icons
 *                   can be provided using [ThemeSwitchIcon] implementations.
 * @param modifier The modifier to be applied to the switch container. Note that due to the
 *                 popup rendering, some layout modifiers may not behave as expected.
 * @param iconSize The target size of the internal icon. Defaults to 20.dp.
 * @param iconScale The scale factor to apply to the icon. Defaults to 1.0f (no scaling).
 *                  Use values greater than 1.0f to enlarge the icon or less than 1.0f to shrink it.
 * @param iconShape The shape to which the switch will be clipped.
 * @param iconTint The tint that will be applied to the icon on the switch.
 */
@Composable
@ExperimentalThemeSwitchApi
fun ThemeAnimationLayoutScope.ThemeSwitch(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon = DefaultButtonIcon,
    modifier: Modifier = Modifier,
    iconSize: DpSize = DpSize(20.dp, 20.dp),
    iconScale: Float = 1f,
    iconShape: Shape = RoundedCornerShape(50),
    iconTint: Color = MaterialTheme.colorScheme.primary,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
) {
    ThemeSwitchWrapper(
        animationState = animationState,
        buttonIcon = buttonIcon,
        modifier = modifier,
        iconSize = iconSize,
        iconScale = iconScale,
        iconTint = iconTint,
        iconShape = iconShape,
        buttonSwitchType = ButtonSwitchType.SwitchOnly,
        indication = indication,
        interactionSource = interactionSource
    )
}

@Composable
internal fun ThemeSwitchBase(
    icon: ThemeSwitchIcon,
    animationState: ThemeAnimationState,
    iconTint: Color,
    iconShape: Shape,
    iconSize: DpSize,
    iconScale: Float,
    modifier: Modifier,
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()

    icon.Icon(
        state = animationState,
        tint = iconTint,
        contentDescription = "Theme switch icon",
        modifier = modifier
            .clip(iconShape)
            .size(iconSize)
            .clickable(
                interactionSource = interactionSource,
                indication = indication
            ) {
                animationState.toggleTheme(
                    isSystemInDarkTheme = isSystemInDarkTheme,
                    switchMode = icon.switchMode
                )
            }
            .scale(iconScale),
    )
}