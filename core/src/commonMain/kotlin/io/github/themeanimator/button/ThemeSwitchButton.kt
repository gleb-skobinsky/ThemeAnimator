package io.github.themeanimator.button

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import io.github.themeanimator.ThemeAnimationState
import io.github.themeanimator.defaulticons.MoonIcon
import io.github.themeanimator.defaulticons.SunIcon

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
fun ThemeSwitchButton(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon = DefaultButtonIcon,
    modifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    iconScale: Float = 1f,
    iconTint: Color = MaterialTheme.colorScheme.primary,
) {
    val iconSize = LocalMinimumInteractiveComponentSize.current
    val positionProvider = remember { ThemeSwitchPositionProvider() }
    val isAnimating = animationState.isAnimating

    val iconContent = remember(
        animationState,
        iconTint,
        iconSize,
        iconScale,
        buttonIcon,
        positionProvider
    ) {
        movableContentOf {
            val isSystemInDarkTheme = isSystemInDarkTheme()
            IconButton(
                onClick = {
                    animationState.toggleTheme(
                        isSystemInDarkTheme = isSystemInDarkTheme,
                        switchMode = buttonIcon.switchMode
                    )
                },
                modifier = Modifier.themeAnimationTarget(
                    state = animationState,
                    positionProvider = positionProvider,
                ),
            ) {
                buttonIcon.Icon(
                    state = animationState,
                    tint = iconTint,
                    contentDescription = "Theme switch icon",
                    modifier = Modifier.size(iconSize).scale(iconScale),
                )
            }
        }
    }

    Box(
        modifier.defaultMinSize(
            minWidth = iconSize,
            minHeight = iconSize
        )
    ) {
        OptionalPopup(
            positionProvider = positionProvider,
            enabled = isAnimating,
            content = iconContent,
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

@Composable
internal fun OptionalPopup(
    positionProvider: PopupPositionProvider,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    if (enabled) {
        Popup(
            popupPositionProvider = positionProvider,
            properties = PopupProperties(
                focusable = false,
                dismissOnClickOutside = false,
                clippingEnabled = false,
            ),
            content = content
        )
    } else {
        content()
    }
}
