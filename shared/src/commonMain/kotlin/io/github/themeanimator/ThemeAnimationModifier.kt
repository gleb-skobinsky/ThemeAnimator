package io.github.themeanimator

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

internal fun <T> Modifier.themeAnimation(
    state: ThemeAnimationState<T>,
) = this then ThemeAnimationElement(state)

internal data class ThemeAnimationElement<T>(
    val state: ThemeAnimationState<T>,
) : ModifierNodeElement<ThemeAnimationNode<T>>() {
    override fun create(): ThemeAnimationNode<T> {
        return ThemeAnimationNode(state)
    }

    override fun update(node: ThemeAnimationNode<T>) {
        node.updateState(state)
    }
}

internal class ThemeAnimationNode<T>(
    private var state: ThemeAnimationState<T>,
) : Modifier.Node(), DrawModifierNode {

    override fun onAttach() {
        super.onAttach()
        observeTheme()
    }

    fun updateState(
        newState: ThemeAnimationState<T>,
    ) {
        state = newState
        observeTheme()
    }

    private var animationObserverJob: Job? = null
    private fun observeTheme() {
        animationObserverJob?.cancel()
        animationObserverJob = coroutineScope.launch {
            merge(state.animationProgress, state.isAnimating).collect {
                invalidateDraw()
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val old = state.prevImageBitmap
        val new = state.currentImageBitmap
        val alpha = state.animationProgress.value
        val isAnimating = state.isAnimating.value

        if (old != null && isAnimating) {
            drawImage(old, alpha = 1f)
        }
        if (new != null && isAnimating) {
            drawImage(new, alpha = alpha)
        }

        if (!isAnimating || old == null) {
            drawContent()
        }
    }
}