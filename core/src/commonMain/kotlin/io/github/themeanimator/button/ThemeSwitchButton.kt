package io.github.themeanimator.button

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.RippleDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.defaulticons.MoonIcon
import io.github.themeanimator.defaulticons.SunIcon
import io.github.themeanimator.layout.ButtonProperties
import io.github.themeanimator.layout.ButtonSwitchType
import io.github.themeanimator.layout.ThemeAnimationLayoutScope

/**
 * The default icon configuration for the theme switch button.
 *
 * This property provides a [ThemeSwitchIcon.DuoVector] displaying the sun icon for dark theme
 * and moon icon for light theme. It serves as a convenient default when no custom icon is specified.
 */
val DefaultButtonIcon = ThemeSwitchIcon.DuoVector(
    darkVector = SunIcon,
    lightVector = MoonIcon
)

/**
 * A composable button that toggles between light and dark themes with animation.
 *
 * This button displays a theme-aware icon and, when clicked, triggers a theme transition
 * animation through the provided [animationState]. The button is rendered in a popup
 * to ensure proper position tracking for animations that require press position information
 * (such as [io.github.themeanimator.ThemeAnimationFormat.CircularAroundPress]).
 *
 * The button uses Material Design's [IconButton] component and automatically updates its
 * icon based on the current theme state.
 *
 * @param animationState The [ThemeAnimationState] that controls the theme switch animation.
 *                       This state tracks the current theme and manages the animation lifecycle.
 * @param buttonIcon The icon to display on the button. Defaults to [DefaultButtonIcon] which
 *                   shows a sun icon for dark theme and moon icon for light theme. Custom icons
 *                   can be provided using [ThemeSwitchIcon] implementations.
 * @param modifier The modifier to be applied to the button container. Note that due to the
 *                 popup rendering, some layout modifiers may not behave as expected.
 * @param iconSize The target size of the internal icon. Defaults to 20.dp.
 * @param iconScale The scale factor to apply to the icon. Defaults to 1.0f (no scaling).
 *                  Use values greater than 1.0f to enlarge the icon or less than 1.0f to shrink it.
 * @param iconTint The tint that will be applied to the icon on the button.
 */
@Composable
@ExperimentalThemeSwitchApi
fun ThemeAnimationLayoutScope.ThemeSwitchButton(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon = DefaultButtonIcon,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    iconScale: Float = 1f,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
) {
    ThemeSwitchWrapper(
        animationState = animationState,
        buttonIcon = buttonIcon,
        modifier = modifier,
        iconShape = RectangleShape,
        iconSize = DpSize(width = iconSize, height = iconSize),
        iconScale = iconScale,
        iconTint = iconTint,
        buttonSwitchType = ButtonSwitchType.IconButton,
        indication = indication,
        interactionSource = interactionSource
    )
}

// private val FixedRippleColor = Color.Black.copy(alpha = 0.3f)
@PublishedApi
internal val FixedRippleConfiguration = RippleConfiguration(
    color = Color.Transparent,
    rippleAlpha = RippleDefaults.RippleAlpha,
)

@Composable
internal fun ThemeAnimationLayoutScope.ThemeSwitchWrapper(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon,
    modifier: Modifier,
    iconSize: DpSize,
    iconScale: Float,
    iconTint: Color,
    iconShape: Shape,
    buttonSwitchType: ButtonSwitchType,
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
) {
    SkippingLayout(
        modifier = Modifier.themeSwitchButtonTracker(
            buttonProperties = ButtonProperties(
                type = buttonSwitchType,
                icon = buttonIcon,
                modifier = modifier,
                iconSize = iconSize,
                iconScale = iconScale,
                iconTint = iconTint,
                iconShape = iconShape,
                indication = indication,
                interactionSource = interactionSource,
            ),
            animationState = animationState
        ),
        shouldSkip = animationState.isAnimating,
        content = {
            TypeBasedGenericSwitch(
                animationState = animationState,
                buttonIcon = buttonIcon,
                modifier = modifier,
                iconSize = iconSize,
                iconScale = iconScale,
                iconTint = iconTint,
                iconShape = iconShape,
                buttonSwitchType = buttonSwitchType,
                interactionSource = interactionSource,
                indication = indication
            )
        }
    )
}

@Composable
@PublishedApi
internal fun TypeBasedGenericSwitch(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon,
    modifier: Modifier,
    iconSize: DpSize,
    iconScale: Float,
    iconTint: Color,
    iconShape: Shape,
    buttonSwitchType: ButtonSwitchType,
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
) {
    when (buttonSwitchType) {
        ButtonSwitchType.IconButton -> {
            ThemeSwitchButtonBase(
                animationState = animationState,
                icon = buttonIcon,
                modifier = modifier,
                iconSize = iconSize,
                iconScale = iconScale,
                iconTint = iconTint,
                indication = indication,
                interactionSource = interactionSource,
            )
        }

        ButtonSwitchType.SwitchOnly -> {
            ThemeSwitchBase(
                icon = buttonIcon,
                animationState = animationState,
                iconTint = iconTint,
                iconShape = iconShape,
                iconSize = iconSize,
                iconScale = iconScale,
                modifier = modifier,
                indication = indication,
                interactionSource = interactionSource
            )
        }
    }
}

@Composable
@PublishedApi
internal fun SkippingLayout(
    shouldSkip: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        measurePolicy = { measurables, constraints ->
            var maxWidth = 0
            var maxHeight = 0
            val placeables = measurables.map { measurable ->
                val placeable = measurable.measure(constraints)
                val newWidth = placeable.measuredWidth
                if (newWidth > maxWidth) {
                    maxWidth = newWidth
                }
                val newHeight = placeable.measuredHeight
                if (newHeight > maxHeight) {
                    maxHeight = newHeight
                }
                placeable
            }
            layout(width = maxWidth, height = maxHeight) {
                if (!shouldSkip) {
                    placeables.onEach { it.place(0, 0) }
                }
            }
        },
        content = content
    )
}

@Composable
@PublishedApi
internal fun ThemeSwitchButtonBase(
    animationState: ThemeAnimationState,
    icon: ThemeSwitchIcon,
    modifier: Modifier,
    iconTint: Color,
    iconSize: DpSize,
    iconScale: Float,
    indication: Indication?,
    interactionSource: MutableInteractionSource?,
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    ThemeIconButton(
        onClick = {
            animationState.toggleTheme(
                isSystemInDarkTheme = isSystemInDarkTheme,
                switchMode = icon.switchMode
            )
        },
        modifier = modifier,
        indication = indication,
        interactionSource = interactionSource,
    ) {
        icon.Icon(
            state = animationState,
            tint = iconTint,
            contentDescription = "Theme switch icon",
            modifier = Modifier
                .size(iconSize)
                .scale(iconScale),
        )
    }
}

/**
 * Registers
 */
internal expect fun Modifier.themeAnimationTarget(
    state: ThemeAnimationState,
    positionProvider: ThemeSwitchPositionProvider,
): Modifier
