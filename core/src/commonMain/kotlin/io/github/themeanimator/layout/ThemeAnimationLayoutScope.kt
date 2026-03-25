package io.github.themeanimator.layout


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize

abstract class ThemeAnimationLayoutScope {
    internal abstract val buttonProperties: ButtonProperties?

    internal abstract val buttonPosition: ButtonPosition?

    internal abstract fun updateButton(
        properties: ButtonProperties,
    )

    internal abstract fun updatePosition(
        position: ButtonPosition
    )

    internal fun Modifier.themeSwitchButtonTracker(
        properties: ButtonProperties,
    ) = this then ThemeSwitchButtonElement(
        properties = properties,
        layoutScope = this@ThemeAnimationLayoutScope
    )
}

@PublishedApi
internal class ThemeAnimationLayoutScopeImpl : ThemeAnimationLayoutScope() {

    @PublishedApi
    override var buttonProperties: ButtonProperties? by mutableStateOf(null)

    @PublishedApi
    override var buttonPosition: ButtonPosition? by mutableStateOf(null)

    override fun updateButton(
        properties: ButtonProperties,
    ) {
        buttonProperties = properties
    }

    override fun updatePosition(position: ButtonPosition) {
        buttonPosition = position
    }
}

internal data class ThemeSwitchButtonElement(
    val properties: ButtonProperties,
    val layoutScope: ThemeAnimationLayoutScope,
) : ModifierNodeElement<ThemeSwitchButtonNode>() {
    override fun create(): ThemeSwitchButtonNode {
        return ThemeSwitchButtonNode(
            layoutScope = layoutScope,
            properties = properties,
        )
    }

    override fun update(node: ThemeSwitchButtonNode) {
        node.updateState(
            newScope = layoutScope,
            newProperties = properties
        )
    }
}

internal class ThemeSwitchButtonNode(
    private var layoutScope: ThemeAnimationLayoutScope,
    private var properties: ButtonProperties,
) : Modifier.Node(),
//    GlobalPositionAwareModifierNode,
    LayoutAwareModifierNode,
    CompositionLocalConsumerModifierNode {
    override fun onAttach() {
        super.onAttach()
        updateScopeState()
    }

    private fun updateScopeState() {
        layoutScope.updateButton(properties)
    }

    fun updateState(
        newScope: ThemeAnimationLayoutScope,
        newProperties: ButtonProperties,
    ) {
        if (layoutScope != newScope) {
            layoutScope = newScope
        }
        if (properties != newProperties) {
            properties = newProperties
        }
        updateScopeState()
    }

    override fun onPlaced(coordinates: LayoutCoordinates) {
        val bounds = coordinates.boundsInWindow()
        layoutScope.buttonProperties?.animationState?.updateButtonPosition(bounds)
        val density = currentValueOf(LocalDensity)
        with(density) {
            layoutScope.updatePosition(
                ButtonPosition(
                    offset = DpOffset(
                        x = bounds.topLeft.x.toDp(),
                        y = bounds.topLeft.y.toDp()
                    ),
                    size = DpSize(
                        width = bounds.size.width.toDp(),
                        height = bounds.size.height.toDp()
                    )
                )
            )
        }
    }

//    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
//        val bounds = coordinates.boundsInWindow()
//
//    }
}
