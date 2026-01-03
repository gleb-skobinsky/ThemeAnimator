package io.github.themeanimator.button

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupPositionProvider
import io.github.themeanimator.ThemeAnimationState

internal class ThemeSwitchPositionProvider(
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