package io.github.themeanimator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val size = MutableStateFlow(IntSize.Zero)

    private val canvas = size.map {
        Canvas(ImageBitmap(it.width, it.height))
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = InitialCanvas
    )

    override fun onAttach() {
        super.onAttach()
        coroutineScope.launch {
            currentState.currentTheme.collectLatest { theme ->
                canvas.value
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

    private companion object {
        val InitialCanvas = Canvas(ImageBitmap(0, 0))
    }
}