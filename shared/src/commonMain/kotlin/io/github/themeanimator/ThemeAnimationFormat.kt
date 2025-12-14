package io.github.themeanimator

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import kotlin.math.max

@Stable
interface ThemeAnimationFormat {
    fun DrawScope.drawAnimationLayer(
        image: ImageBitmap,
        progress: Float,
    )

    object Crossfade : ThemeAnimationFormat {
        override fun DrawScope.drawAnimationLayer(image: ImageBitmap, progress: Float) {
            drawImage(
                image = image,
                alpha = progress
            )
        }
    }

    object Circular : ThemeAnimationFormat {
        override fun DrawScope.drawAnimationLayer(image: ImageBitmap, progress: Float) {
            val originalRadius = max(size.width, size.height)
            val radius = originalRadius * progress

            val center = size.center
            val circlePath = Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        center = center,
                        radius = radius
                    )
                )
            }

            clipPath(circlePath) {
                drawImage(image)
            }
        }
    }
    object Sliding : ThemeAnimationFormat {
        override fun DrawScope.drawAnimationLayer(image: ImageBitmap, progress: Float) {
            val clipWidth = size.width * progress
            clipRect(right = clipWidth) {
                drawImage(image)
            }
        }
    }
}