package io.github.themeanimator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

private object ThemeSwitchPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        // Position the popup below the anchor aligned horizontally with the anchor's
        // center.
        return IntOffset(
            x = anchorBounds.left + anchorBounds.width / 2,
            y = anchorBounds.top + anchorBounds.height * 2,
        )
    }
}

@Composable
fun ThemeSwitchButton(
    animationState: ThemeAnimationState,
    darkThemeIcon: ImageVector = MoonIcon,
    lightThemeIcon: ImageVector = SunIcon,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Popup(
            popupPositionProvider = ThemeSwitchPositionProvider,
            properties = PopupProperties(focusable = false, dismissOnClickOutside = false)
        ) {
            ThemeSwitchButtonBase(
                animationState = animationState,
                lightThemeIcon = lightThemeIcon,
                darkThemeIcon = darkThemeIcon
            )
        }
    }
}

@Composable
private fun ThemeSwitchButtonBase(
    animationState: ThemeAnimationState,
    lightThemeIcon: ImageVector,
    darkThemeIcon: ImageVector,
) {
    IconButton(
        interactionSource = animationState.interactionSource,
        onClick = {
            animationState.toggleTheme()
        },
        modifier = Modifier.themeAnimationButtonTarget(animationState)
    ) {
        Icon(
            imageVector = if (animationState.isDark) {
                lightThemeIcon
            } else {
                darkThemeIcon
            },
            contentDescription = "Moon icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Stable
fun Modifier.themeAnimationButtonTarget(
    animationState: ThemeAnimationState,
) = onGloballyPositioned { coordinates ->
    val position = coordinates.positionInRoot()
    val size = coordinates.size
    animationState.updateButtonPosition(
        position.x + size.width / 2f,
        position.y + size.height / 2f
    )
}