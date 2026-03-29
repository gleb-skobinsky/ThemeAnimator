package io.github.themeanimator.button

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ObserverModifierNode
import androidx.compose.ui.node.invalidateMeasurement
import androidx.compose.ui.node.observeReads
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import io.github.themeanimator.layout.ButtonPosition
import io.github.themeanimator.layout.ThemeAnimationLayoutScope

@PublishedApi
internal fun Modifier.realSwitchLayout(
    scope: ThemeAnimationLayoutScope,
) = this then SwitchLayoutElement(scope)

private data class SwitchLayoutElement(
    val scope: ThemeAnimationLayoutScope,
) : ModifierNodeElement<SwitchLayoutNode>() {
    override fun create(): SwitchLayoutNode {
        return SwitchLayoutNode(scope)
    }

    override fun update(node: SwitchLayoutNode) {
        node.updateState(scope)
    }
}

private class SwitchLayoutNode(
    private var layoutScope: ThemeAnimationLayoutScope,
) : Modifier.Node(), LayoutModifierNode, ObserverModifierNode {
    private var currentPosition: ButtonPosition? = null

    override fun onObservedReadsChanged() {
        observeButtonPosition()
        invalidateMeasurement()
    }

    private fun observeButtonPosition() {
        observeReads { currentPosition = layoutScope.buttonPosition }
    }

    fun updateState(
        newScope: ThemeAnimationLayoutScope
    ) {
        layoutScope = newScope
    }

    override fun onAttach() {
        observeButtonPosition()
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val offset = layoutScope.buttonPosition?.offset ?: DpOffset.Zero
        val intOffset = IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
        val size = layoutScope.buttonPosition?.size ?: DpSize.Zero
        val w = size.width.roundToPx()
        val h = size.height.roundToPx()
        val newConstraints = constraints.copy(
            minWidth = w,
            minHeight = h,
            maxWidth = w,
            maxHeight = h,
        )
        val placeable = measurable.measure(newConstraints)
        return layout(w, h) {
            placeable.place(x = intOffset.x, y = intOffset.y)
        }
    }
}