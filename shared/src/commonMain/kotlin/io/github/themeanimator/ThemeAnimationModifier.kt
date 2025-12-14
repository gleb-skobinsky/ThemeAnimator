package io.github.themeanimator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import kotlinx.coroutines.flow.collectLatest
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
    private var currentState: ThemeAnimationState<T>,
    private var currentContent: @Composable () -> Unit,
) : Modifier.Node(), DrawModifierNode {
    private val canvas = Canvas(ImageBitmap())
    override fun onAttach() {
        super.onAttach()
        coroutineScope.launch {
            currentState.currentTheme.collectLatest { theme ->

            }
        }
    }

    fun updateState(
        newState: ThemeAnimationState<T>,
        newContent: @Composable () -> Unit,
    ) {
        currentState = newState
        currentContent = newContent
        //invalidateDraw()
    }

    override fun ContentDrawScope.draw() {

    }
}