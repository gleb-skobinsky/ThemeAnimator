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
        return ThemeAnimationNode(graphicsLayer, state, isDark)
    }

    override fun update(node: ThemeAnimationNode) {
        node.updateState(graphicsLayer, state, isDark)
    }
}

internal class ThemeAnimationNode(
    private var graphicsLayer: GraphicsLayer,
    private var state: ThemeAnimationState,
    private var isDark: Boolean,
) : Modifier.Node(), DrawModifierNode, LayoutAwareModifierNode {

    private var animationProgress = 0f
    private var prevImageBitmap: ImageBitmap? = null
    private var currentImageBitmap: ImageBitmap? = null

    private var animationPhase = AnimationPhase.Idle

    private enum class AnimationPhase {
        Idle,
        InterceptDraw,
        Animate,
    }

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
                    RecordStatus.RecordRequested -> {
                        recordInitialImage()
                        state.requestRecord.value = RecordStatus.Recorded
                    }

                    RecordStatus.PrepareForAnimation -> {
                        // Immediately intercept drawing to avoid flickering of the new theme before animation
                        // The actual animation command is expected to come
                        // from recomposition through node update
                        animationPhase = AnimationPhase.InterceptDraw
                    }

                    RecordStatus.Initial,
                    RecordStatus.Recorded -> Unit
                }
            }
        }
    }

    fun updateState(
        newGraphicsLayer: GraphicsLayer,
        newState: ThemeAnimationState,
        newIsDark: Boolean,
    ) {
        graphicsLayer = newGraphicsLayer
        when {
            state != newState -> {
                state = newState
                observeRecordRequests()
            }

            isDark != newIsDark -> {
                isDark = newIsDark
                runAnimation()
            }
        }
    }

    private var animationJob: Job? = null

    private fun runAnimation() {
        animationJob?.cancel()
        animationJob = coroutineScope.launch {
            runAnimationSuspend()
        }
    }

    private suspend fun runAnimationSuspend() {
        animationProgress = 0f
        animationPhase = AnimationPhase.Animate
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
        animationPhase = AnimationPhase.Idle
        invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        val old = prevImageBitmap
        val new = currentImageBitmap
        val progress = animationProgress
        val phase = animationPhase
        val position = state.buttonPosition

        when (phase) {
            AnimationPhase.InterceptDraw if old != null -> {
                drawImage(old)
            }
            AnimationPhase.Animate if old != null && new != null -> {
                drawImage(old)
                with(state.format) {
                    drawAnimationLayer(
                        image = new,
                        progress = progress,
                        pressPosition = position,
                        useDynamicContent = state.useDynamicContent
                    )
                }
            }

            else -> {
                // Record content to graphicsLayer for future snapshots
                graphicsLayer.record {
                    this@draw.drawContent()
                }
                // Draw the recorded layer
                drawLayer(graphicsLayer)
            }
        }
    }

    private suspend fun recordInitialImage() {
        val image = graphicsLayer.toImageBitmap()
        prevImageBitmap = image
        currentImageBitmap = image
    }
}