package io.github.themeanimator.button

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import io.github.themeanimator.ThemeAnimationState

internal actual fun Modifier.themeAnimationTarget(
    state: ThemeAnimationState,
    positionProvider: ThemeSwitchPositionProvider,
): Modifier = this.onGloballyPositioned { coordinates ->
    state.updateButtonPosition(coordinates.boundsInWindow().center)
}