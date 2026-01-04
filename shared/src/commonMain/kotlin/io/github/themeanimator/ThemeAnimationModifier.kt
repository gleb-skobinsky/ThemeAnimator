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

internal fun Modifier.themeAnimation(
    state: ThemeAnimationState,
    isDark: Boolean,
    graphicsLayer: GraphicsLayer,
) = this then ThemeAnimationElement(
    graphicsLayer = graphicsLayer,
    state = state,
    isDark = isDark
)

internal data class ThemeAnimationElement(
    val graphicsLayer: GraphicsLayer,
    val state: ThemeAnimationState,
    val isDark: Boolean,
) : ModifierNodeElement<ThemeAnimationNode>() {
    override fun create(): ThemeAnimationNode {
        return ThemeAnimationNode(graphicsLayer, state)
    }

    override fun update(node: ThemeAnimationNode) {
        node.updateState(graphicsLayer, state)
    }
}

internal class ThemeAnimationNode(
    private var graphicsLayer: GraphicsLayer,
    private var state: ThemeAnimationState,
) : Modifier.Node(), DrawModifierNode, LayoutAwareModifierNode {

    private var animationProgress = 0f
    private var prevImageBitmap: ImageBitmap? = null
    private var currentImageBitmap: ImageBitmap? = null

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
                when (request) {
                    RecordStatus.Initial,
                    RecordStatus.Recorded -> Unit

                    RecordStatus.RecordRequested -> {
                        recordInitialImage()
                        state.requestRecord.value = RecordStatus.Recorded
                    }

                    RecordStatus.AnimationRequested -> runAnimation()
                }
            }
        }
    }

    fun updateState(
        newGraphicsLayer: GraphicsLayer,
        newState: ThemeAnimationState,
    ) {
        graphicsLayer = newGraphicsLayer
        state = newState
        observeRecordRequests()
    }

    private suspend fun runAnimation() {
        animationProgress = 0f
        isAnimating = true
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