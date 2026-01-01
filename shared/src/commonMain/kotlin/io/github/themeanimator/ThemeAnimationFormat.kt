package io.github.themeanimator

import androidx.collection.LruCache
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import kotlin.math.hypot
import kotlin.math.max

@Immutable
interface ThemeAnimationFormat {
    fun ContentDrawScope.drawAnimationLayer(
        progress: Float,
        pressPosition: Offset?,
    )

    object Crossfade : ThemeAnimationFormat {
        override fun ContentDrawScope.drawAnimationLayer(
            progress: Float,
            pressPosition: Offset?,
        ) {
            drawIntoCanvas { canvas ->
                val paint = Paint().apply {
                    alpha = progress
                }

                canvas.saveLayer(size.toRect(), paint)
                drawContent()
                canvas.restore()
            }
        }
    }

    object Circular : ThemeAnimationFormat {
        override fun ContentDrawScope.drawAnimationLayer(
            progress: Float,
            pressPosition: Offset?,
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
                this@drawAnimationLayer.drawContent()
            }
        }
    }

    object Sliding : ThemeAnimationFormat {
        override fun ContentDrawScope.drawAnimationLayer(
            progress: Float,
            pressPosition: Offset?,
        ) {
            val clipWidth = size.width * progress
            clipRect(right = clipWidth) {
                this@drawAnimationLayer.drawContent()
            }
        }
    }

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
            progress: Float,
            pressPosition: Offset?,
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
//                drawImage(image)
                this@drawAnimationLayer.drawContent()
            }
        }
    }
}