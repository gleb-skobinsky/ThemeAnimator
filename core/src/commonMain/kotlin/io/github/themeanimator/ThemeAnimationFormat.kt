package io.github.themeanimator

import androidx.collection.LruCache
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import kotlin.math.hypot
import kotlin.math.max

/**
 * Defines the visual format and behavior of theme transition animations.
 *
 * Implementations of this interface determine how the theme change is animated,
 * including clipping shapes, masking, and visual effects. Each format provides
 * a unique way to transition between light and dark themes.
 *
 * The implementations contained in this file are some commonly-used formats.
 * Implement your own [ThemeAnimationFormat] to further customize animation behavior.
 */
@Immutable
interface ThemeAnimationFormat {
    /**
     * Draws the animated layer during a theme transition.
     *
     * This method is called during the animation frame to render the new theme
     * over the old one. The implementation determines the visual effect of the transition.
     *
     * @param image The image bitmap containing the new theme's visual representation.
     * @param progress The animation progress value, ranging from 0.0f (start) to 1.0f (complete).
     * @param pressPosition The screen position where the theme toggle was triggered. May be null
     *                     if position tracking is not available.
     * @param useDynamicContent If true, draw dynamic content from the new theme; otherwise draw
     *                          from the static [image] bitmap. Dynamic content preserves
     *                          animations and scrolling during the transition
     *                          (note that it is only true for the target theme part).
     */
    fun ContentDrawScope.drawAnimationLayer(
        image: ImageBitmap,
        progress: Float,
        pressPosition: Offset?,
        useDynamicContent: Boolean,
    )

    /**
     * A crossfade animation format that gradually fades between themes.
     */
    object Crossfade : ThemeAnimationFormat {
        override fun ContentDrawScope.drawAnimationLayer(
            image: ImageBitmap,
            progress: Float,
            pressPosition: Offset?,
            useDynamicContent: Boolean,
        ) {
            if (useDynamicContent) {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        alpha = progress
                    }

                    canvas.saveLayer(size.toRect(), paint)
                    drawContent()
                    canvas.restore()
                }
            } else {
                drawImage(
                    image = image,
                    alpha = progress
                )
            }
        }
    }

    /**
     * A circular reveal animation format that expands from the center of the screen.
     */
    object Circular : ThemeAnimationFormat {
        override fun ContentDrawScope.drawAnimationLayer(
            image: ImageBitmap,
            progress: Float,
            pressPosition: Offset?,
            useDynamicContent: Boolean,
        ) {
            val originalRadius = max(size.width, size.height)
            val radius = originalRadius * progress

            val center = size.center
            val circlePath = Path().apply {
                addOval(
                    Rect(
                        center = center,
                        radius = radius
                    )
                )
            }

            clipPath(circlePath) {
                if (useDynamicContent) {
                    this@drawAnimationLayer.drawContent()
                } else {
                    drawImage(image)
                }
            }
        }
    }

    /**
     * A sliding animation format that reveals the new theme from left to right.
     */
    object Sliding : ThemeAnimationFormat {
        override fun ContentDrawScope.drawAnimationLayer(
            image: ImageBitmap,
            progress: Float,
            pressPosition: Offset?,
            useDynamicContent: Boolean,
        ) {
            val clipWidth = size.width * progress
            clipRect(right = clipWidth) {
                if (useDynamicContent) {
                    this@drawAnimationLayer.drawContent()
                } else {
                    drawImage(image)
                }
            }
        }
    }

    /**
     * A circular reveal animation format that expands from the button press position.
     *
     * This format reveals the new theme through a circular clipping mask that expands
     * outward from the position where the theme toggle button was pressed. This creates
     * a ripple-like effect emanating from the user's interaction point. If no press
     * position is available, the animation falls back to expanding from the center.
     */
    object CircularAroundPress : ThemeAnimationFormat {

        private val maxRadiusCache = LruCache<Pair<Offset, Size>, Float>(3)

        private inline fun <K : Any, V : Any> LruCache<K, V>.getOrPut(
            key: K,
            onCreate: (K) -> V
        ): V {
            val value = get(key)
            if (value != null) {
                return value
            }
            val newValue = onCreate(key)
            put(key, newValue)
            return newValue
        }


        override fun ContentDrawScope.drawAnimationLayer(
            image: ImageBitmap,
            progress: Float,
            pressPosition: Offset?,
            useDynamicContent: Boolean,
        ) {
            val center = pressPosition ?: size.center

            val maxRadius = maxRadiusCache.getOrPut(
                center to size
            ) { (center, size) ->
                val farthestX = if (center.x < size.width / 2) size.width else 0f
                val farthestY = if (center.y < size.height / 2) size.height else 0f
                hypot(farthestX - center.x, farthestY - center.y)
            }

            val radius = maxRadius * progress

            val circlePath = Path().apply {
                addOval(
                    Rect(
                        center = center,
                        radius = radius
                    )
                )
            }

            clipPath(circlePath) {
                if (useDynamicContent) {
                    this@drawAnimationLayer.drawContent()
                } else {
                    drawImage(image)
                }
            }
        }
    }
}