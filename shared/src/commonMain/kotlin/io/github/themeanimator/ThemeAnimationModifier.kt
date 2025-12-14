package io.github.themeanimator

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal fun <T> Modifier.themeAnimation(

    theme: T,
    graphicsLayer: GraphicsLayer,
) = this then ThemeAnimationElement(theme, graphicsLayer)

internal data class ThemeAnimationElement<T>(
    val theme: T,
    val graphicsLayer: GraphicsLayer,
) : ModifierNodeElement<ThemeAnimationNode<T>>() {
    override fun create(): ThemeAnimationNode<T> {
        return ThemeAnimationNode(theme, graphicsLayer)
    }

    override fun update(node: ThemeAnimationNode<T>) {
        node.updateState(theme, graphicsLayer)
    }
}

internal class ThemeAnimationNode<T>(
    private var theme: T,
    private var graphicsLayer: GraphicsLayer,
) : Modifier.Node(), DrawModifierNode {

    private var isAnimating = false
    private var animationProgress = 0f
    private var prevImageBitmap: ImageBitmap? = null
    private var currentImageBitmap: ImageBitmap? = null
    private var prevTheme: T? = null

    private var animationObserverJob: Job? = null

    override fun onAttach() {
        super.onAttach()
        coroutineScope.launch {
            val image = graphicsLayer.toImageBitmap()
            prevImageBitmap = image
            currentImageBitmap = image
        }
    }

    fun updateState(
        newTheme: T,
        newGraphicsLayer: GraphicsLayer,
    ) {
        graphicsLayer = newGraphicsLayer
        prevTheme = theme
        theme = newTheme
        if (prevTheme != theme) {
            runAnimation()
        }
    }

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
            graphicsLayer.record {
                this@draw.drawContent()
            }
            drawContent()
        }
    }
}