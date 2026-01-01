package io.github.themeanimator

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
import androidx.compose.ui.unit.dp

@Composable
fun ThemeSwitchButton(
    animationState: ThemeAnimationState,
    darkThemeIcon: ImageVector = MoonIcon,
    lightThemeIcon: ImageVector = SunIcon,
    modifier: Modifier = Modifier,
) {
    IconButton(
        interactionSource = animationState.interactionSource,
        onClick = {
            animationState.toggleTheme()
        },
        modifier = modifier.themeAnimationButtonTarget(animationState)
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