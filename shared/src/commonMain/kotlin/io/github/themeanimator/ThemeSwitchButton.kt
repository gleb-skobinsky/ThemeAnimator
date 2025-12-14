package io.github.themeanimator

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp

@Composable
fun ThemeSwitchButton(
    animationState: ThemeAnimationState,
) {
    IconButton(
        interactionSource = animationState.interactionSource,
        onClick = {
            animationState.toggleTheme()
        },
        modifier = Modifier.onGloballyPositioned { coordinates ->
            // Store the button's position in root coordinates
            val position = coordinates.positionInRoot()
            val size = coordinates.size
            // Calculate center of the button in root coordinates
            animationState.updateButtonPosition(
                position.x + size.width / 2f,
                position.y + size.height / 2f
            )
        }
    ) {
        Icon(
            imageVector = MoonIcon,
            contentDescription = "Moon icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}