package io.github.themeanimator

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemeSwitchButton(
    animationState: ThemeAnimationState,
) {
    IconButton(
        interactionSource = animationState.interactionSource,
        onClick = {
            animationState.toggleTheme()
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