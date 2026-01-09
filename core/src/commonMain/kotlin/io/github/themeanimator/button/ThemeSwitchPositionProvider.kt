package io.github.themeanimator.button

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

/**
 * A [PopupPositionProvider] that tracks button position for theme switch animations.
 *
 * This provider calculates popup positioning while internally tracking the button's
 * screen coordinates. The tracked position is used by animation formats that require
 * the interaction point, such as [io.github.themeanimator.ThemeAnimationFormat.CircularAroundPress].
 */
@Stable
internal class ThemeSwitchPositionProvider : PopupPositionProvider {

    /**
     * The current popup position relative to the window.
     * 
     * This position is updated each time [calculatePosition] is called and represents
     * the top-left corner of the popup content.
     */
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