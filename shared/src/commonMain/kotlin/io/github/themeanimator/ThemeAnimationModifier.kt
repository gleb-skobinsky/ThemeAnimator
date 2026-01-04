package io.github.themeanimator

import androidx.compose.animation.core.animate
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal fun <Theme> Modifier.themeAnimation(
    state: ThemeAnimationState,
    theme: Theme,
    graphicsLayer: GraphicsLayer,
) = this then ThemeAnimationElement(
    theme = theme,
    graphicsLayer = graphicsLayer,
    state = state
)

internal data class ThemeAnimationElement<Theme>(
    val theme: Theme,
    val graphicsLayer: GraphicsLayer,
    val state: ThemeAnimationState,
) : ModifierNodeElement<ThemeAnimationNode<Theme>>() {
    override fun create(): ThemeAnimationNode<Theme> {
        return ThemeAnimationNode(theme, graphicsLayer, state)
    }

    override fun update(node: ThemeAnimationNode<Theme>) {
        node.updateState(theme, graphicsLayer, state)
    }
}

internal class ThemeAnimationNode<Theme>(
    private var theme: Theme,
    private var graphicsLayer: GraphicsLayer,
    private var state: ThemeAnimationState,
) : Modifier.Node(), DrawModifierNode, LayoutAwareModifierNode {

    private var animationProgress = 0f
    private var prevImageBitmap: ImageBitmap? = null
    private var currentImageBitmap: ImageBitmap? = null
    private var prevTheme: Theme? = null

    private var isAnimating = false

    override fun onAttach() {
        super.onAttach()
        observeRecordRequests()
    }

    private var recordRequestsJob: Job? = null
    private fun observeRecordRequests() {
        recordRequestsJob?.cancel()
        recordRequestsJob = coroutineScope.launch {
            state.requestRecord.collectLatest { request ->
                // Record the current screen before allowing theme change
                if (request == RecordStatus.RecordRequested) {
                    recordInitialImage()
                    state.requestRecord.value = RecordStatus.Recorded
                }
            }
        }
    }

    fun updateState(
        newTheme: Theme,
        newGraphicsLayer: GraphicsLayer,
        newState: ThemeAnimationState,
    ) {
        graphicsLayer = newGraphicsLayer
        state = newState
        prevTheme = theme
        theme = newTheme
        if (prevTheme != theme) {
            runAnimation()
        }
        observeRecordRequests()
    }

    private var animationObserverJob: Job? = null
    private fun runAnimation() {
        animationProgress = 0f
        isAnimating = true
        animationObserverJob?.cancel()
        animationObserverJob = coroutineScope.launch {
            prevImageBitmap = currentImageBitmap
            currentImageBitmap = graphicsLayer.toImageBitmap()
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = state.animationSpec
            ) { value, _ ->
                animationProgress = value
                invalidateDraw()
            }
            isAnimating = false
            invalidateDraw()
        }
    }

    override fun ContentDrawScope.draw() {
        val old = prevImageBitmap
        val new = currentImageBitmap
        val progress = animationProgress
        val isAnim = isAnimating
        val position = state.buttonPosition

        if (old != null && new != null && isAnim) {
            drawImage(old)
            with(state.format) {
                drawAnimationLayer(
                    image = new,
                    progress = progress,
                    pressPosition = position,
                    useDynamicContent = state.useDynamicContent
                )
            }
        } else {
            // Record content to graphicsLayer for future snapshots
            graphicsLayer.record {
                this@draw.drawContent()
            }
            // Draw the recorded layer
            drawLayer(graphicsLayer)
        }
    }

    private suspend fun recordInitialImage() {
        val image = graphicsLayer.toImageBitmap()
        prevImageBitmap = image
        currentImageBitmap = image
    }
}