package io.github.themeanimator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal fun <T> Modifier.themeAnimation(
    state: ThemeAnimationState<T>,
    content: @Composable () -> Unit,
) = this then ThemeAnimationElement(state, content)

internal data class ThemeAnimationElement<T>(
    val state: ThemeAnimationState<T>,
    val content: @Composable () -> Unit,
) : ModifierNodeElement<ThemeAnimationNode<T>>() {
    override fun create(): ThemeAnimationNode<T> {
        return ThemeAnimationNode(state, content)
    }

    override fun update(node: ThemeAnimationNode<T>) {
        node.updateState(newState = state, newContent = content)
    }
}

internal class ThemeAnimationNode<T>(
    private var state: ThemeAnimationState<T>,
    private var currentContent: @Composable () -> Unit,
) : Modifier.Node(), DrawModifierNode {

    override fun onAttach() {
        super.onAttach()
        observeTheme()
    }

    fun updateState(
        newState: ThemeAnimationState<T>,
        newContent: @Composable () -> Unit,
    ) {
        state = newState
        currentContent = newContent
        observeTheme()
    }

    private var animationObserverJob: Job? = null
    private fun observeTheme() {
        animationObserverJob?.cancel()
        animationObserverJob = coroutineScope.launch {
            state.animationProgress.collect {
                invalidateDraw()
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val old = state.prevImageBitmap
        val new = state.currentImageBitmap
        val alpha = state.animationProgress.value

        if (old != null && state.isAnimating) {
            drawImage(old, alpha = 1f)
        }
        if (new != null && state.isAnimating) {
            drawImage(new, alpha = alpha)
        }

        if (!state.isAnimating || old == null) {
            drawContent()
        }
    }
}