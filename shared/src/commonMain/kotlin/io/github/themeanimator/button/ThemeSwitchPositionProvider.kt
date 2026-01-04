package io.github.themeanimator.button

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.window.PopupPositionProvider

@Stable
internal class ThemeSwitchPositionProvider : PopupPositionProvider {
    internal var popupPosition: Offset = Offset.Zero

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val topLeftX = (anchorBounds.left - popupContentSize.width).toFloat()
        val topLeftY = anchorBounds.top.toFloat()
        popupPosition = Offset(topLeftX, topLeftY)
        return IntOffset(
            x = anchorBounds.left + anchorBounds.width / 2,
            y = anchorBounds.top + anchorBounds.height * 2,
        )
    }
}