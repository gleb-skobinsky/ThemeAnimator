package io.github.themeanimator

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

internal enum class RecordStatus {
    Initial,
    RecordRequested,
    Recorded
}

@Stable
class ThemeAnimationState(
    private val coroutineScope: CoroutineScope,
) {
    var isDark: Boolean by mutableStateOf(false)
        private set

    internal val requestRecord = MutableStateFlow(RecordStatus.Initial)

    fun toggleTheme() {
        coroutineScope.launch {
            requestRecord.value = RecordStatus.RecordRequested
            requestRecord.firstOrNull { it == RecordStatus.Recorded }
            isDark = !isDark
        }
    }
}

@Composable
fun rememberThemeAnimationState(): ThemeAnimationState {
    val coroutineScope = rememberCoroutineScope()
    return remember(coroutineScope) { ThemeAnimationState(coroutineScope) }
}

internal fun <T> Modifier.themeAnimation(
    state: ThemeAnimationState,
    theme: T,
    graphicsLayer: GraphicsLayer,
) = this then ThemeAnimationElement(theme, graphicsLayer, state)

internal data class ThemeAnimationElement<T>(
    val theme: T,
    val graphicsLayer: GraphicsLayer,
    val state: ThemeAnimationState,
) : ModifierNodeElement<ThemeAnimationNode<T>>() {
    override fun create(): ThemeAnimationNode<T> {
        return ThemeAnimationNode(theme, graphicsLayer, state)
    }

    override fun update(node: ThemeAnimationNode<T>) {
        node.updateState(theme, graphicsLayer, state)
    }
}

internal class ThemeAnimationNode<T>(
    private var theme: T,
    private var graphicsLayer: GraphicsLayer,
    private var state: ThemeAnimationState,
) : Modifier.Node(), DrawModifierNode, LayoutAwareModifierNode {

    private var isAnimating = false
    private var animationProgress = 0f
    private var prevImageBitmap: ImageBitmap? = null
    private var currentImageBitmap: ImageBitmap? = null
    private var prevTheme: T? = null

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
                    invalidateDraw()
                    recordInitialImage()
                    state.requestRecord.value = RecordStatus.Recorded
                }
            }
        }
    }

    fun updateState(
        newTheme: T,
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
        isAnimating = true
        animationObserverJob?.cancel()
        animationObserverJob = coroutineScope.launch {
            prevImageBitmap = currentImageBitmap
            currentImageBitmap = graphicsLayer.toImageBitmap()
            animate(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = tween(3000)
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
        val alpha = animationProgress
        val isAnim = isAnimating

        if (old != null && new != null && isAnim) {
            drawImage(old, alpha = 1f)
            drawImage(new, alpha = alpha)
        } else {
            // Record content to graphicsLayer for future snapshots
            graphicsLayer.record {
                this@draw.drawContent()
            }
            // Draw the recorded layer
            drawLayer(graphicsLayer)
        }
    }

    private fun recordInitialImage() {
        coroutineScope.launch {
            val image = graphicsLayer.toImageBitmap()
            prevImageBitmap = image
            currentImageBitmap = image
        }
    }
}