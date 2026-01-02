package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

val DefaultButtonIcon = ThemeSwitchIcon.DuoVector(
    darkVector = SunIcon,
    lightVector = MoonIcon
)

@Composable
fun ThemeSwitchButton(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon = DefaultButtonIcon,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    iconSize: Dp = 20.dp,
    iconScale: Float = 1f,
) {
    Box(modifier) {
        val positionProvider = remember(animationState) {
            ThemeSwitchPositionProvider(animationState)
        }
        Popup(
            popupPositionProvider = positionProvider,
            properties = PopupProperties(focusable = false, dismissOnClickOutside = false)
        ) {
            ThemeSwitchButtonBase(
                animationState = animationState,
                buttonIcon = buttonIcon,
                iconModifier = iconModifier,
                iconSize = iconSize,
                iconScale = iconScale
            )
        }
    }
}

@Composable
private fun ThemeSwitchButtonBase(
    animationState: ThemeAnimationState,
    buttonIcon: ThemeSwitchIcon,
    iconModifier: Modifier,
    iconSize: Dp,
    iconScale: Float,
) {
    IconButton(
        interactionSource = animationState.interactionSource,
        onClick = {
            animationState.toggleTheme()
        },
        modifier = iconModifier
    ) {
        buttonIcon.Icon(
            state = animationState,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "Theme switch icon",
            modifier = Modifier.size(iconSize).scale(iconScale)
        )
    }
}

private class ThemeSwitchPositionProvider(
    private val state: ThemeAnimationState,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        // Position the popup below the anchor aligned horizontally with the anchor's
        // center.
        val offset = IntOffset(
            x = anchorBounds.left + anchorBounds.width / 2,
            y = anchorBounds.top + anchorBounds.height * 2,
        )
        state.updateButtonPosition(
            Rect(
                Offset(
                    (anchorBounds.left - popupContentSize.width).toFloat(),
                    anchorBounds.top.toFloat()
                ),
                popupContentSize.toSize()
            )
        )
        return offset
    }
}