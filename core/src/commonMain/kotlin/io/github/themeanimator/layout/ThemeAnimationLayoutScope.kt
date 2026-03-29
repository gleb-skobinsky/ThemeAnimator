package io.github.themeanimator.layout


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import io.github.themeanimator.ThemeAnimationState

abstract class ThemeAnimationLayoutScope {
    internal abstract val buttonProperties: ButtonProperties?

    internal abstract val buttonPosition: ButtonPosition?

    internal abstract fun updateButton(
        properties: ButtonProperties?,
    )

    internal abstract fun updatePosition(
        position: ButtonPosition
    )

    internal fun Modifier.themeSwitchButtonTracker(
        buttonProperties: ButtonProperties,
        animationState: ThemeAnimationState,
    ) = this then ThemeSwitchButtonElement(
        buttonProperties = buttonProperties,
        layoutScope = this@ThemeAnimationLayoutScope,
        animationState = animationState
    )
}

@PublishedApi
internal class ThemeAnimationLayoutScopeImpl : ThemeAnimationLayoutScope() {

    @PublishedApi
    override var buttonProperties: ButtonProperties? by mutableStateOf(null)

    @PublishedApi
    override var buttonPosition: ButtonPosition? by mutableStateOf(null)

    override fun updateButton(
        properties: ButtonProperties?,
    ) {
        buttonProperties = properties
    }

    override fun updatePosition(position: ButtonPosition) {
        buttonPosition = position
    }
}

internal data class ThemeSwitchButtonElement(
    val buttonProperties: ButtonProperties,
    val layoutScope: ThemeAnimationLayoutScope,
    val animationState: ThemeAnimationState,
) : ModifierNodeElement<ThemeSwitchButtonNode>() {
    override fun create(): ThemeSwitchButtonNode {
        return ThemeSwitchButtonNode(
            layoutScope = layoutScope,
            currentProperties = buttonProperties,
            currentState = animationState,
        )
    }

    override fun update(node: ThemeSwitchButtonNode) {
        node.updateState(
            newScope = layoutScope,
            newContent = buttonProperties,
            newState = animationState,
        )
    }
}

internal class ThemeSwitchButtonNode(
    private var layoutScope: ThemeAnimationLayoutScope,
    private var currentProperties: ButtonProperties?,
    private var currentState: ThemeAnimationState,
) : Modifier.Node(),
    LayoutAwareModifierNode,
    CompositionLocalConsumerModifierNode {
    override fun onAttach() {
        super.onAttach()
        updateScopeState()
    }

    private fun updateScopeState() {
        layoutScope.updateButton(currentProperties)
    }

    fun updateState(
        newScope: ThemeAnimationLayoutScope,
        newContent: ButtonProperties?,
        newState: ThemeAnimationState,
    ) {
        if (layoutScope != newScope) {
            layoutScope = newScope
        }
        if (currentProperties != newContent) {
            currentProperties = newContent
        }
        if (currentState != newState) {
            currentState = newState
        }
        updateScopeState()
    }

    override fun onPlaced(coordinates: LayoutCoordinates) {
        val bounds = coordinates.boundsInWindow()
        currentState.updateButtonPosition(bounds)
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
}
