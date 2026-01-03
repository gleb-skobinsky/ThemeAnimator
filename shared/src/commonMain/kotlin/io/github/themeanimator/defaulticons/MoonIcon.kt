package io.github.themeanimator.defaulticons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * A moon icon suitable for representing the dark theme.
 */
val MoonIcon: ImageVector
    get() {
        if (_MoonIcon != null) {
            return _MoonIcon!!
        }
        _MoonIcon = ImageVector.Builder(
            name = "MoonIcon",
            defaultWidth = 122.88.dp,
            defaultHeight = 122.89.dp,
            viewportWidth = 122.88f,
            viewportHeight = 122.89f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(49.06f, 1.27f)
                curveToRelative(2.17f, -0.45f, 4.34f, -0.77f, 6.48f, -0.98f)
                curveToRelative(2.2f, -0.21f, 4.38f, -0.31f, 6.53f, -0.29f)
                curveToRelative(1.21f, 0.01f, 2.18f, 1f, 2.17f, 2.21f)
                curveToRelative(-0.01f, 0.93f, -0.6f, 1.72f, -1.42f, 2.03f)
                curveToRelative(-9.15f, 3.6f, -16.47f, 10.31f, -20.96f, 18.62f)
                curveToRelative(-4.42f, 8.17f, -6.1f, 17.88f, -4.09f, 27.68f)
                lineToRelative(0.01f, 0.07f)
                curveToRelative(2.29f, 11.06f, 8.83f, 20.15f, 17.58f, 25.91f)
                curveToRelative(8.74f, 5.76f, 19.67f, 8.18f, 30.73f, 5.92f)
                lineToRelative(0.07f, -0.01f)
                curveToRelative(7.96f, -1.65f, 14.89f, -5.49f, 20.3f, -10.78f)
                curveToRelative(5.6f, -5.47f, 9.56f, -12.48f, 11.33f, -20.16f)
                curveToRelative(0.27f, -1.18f, 1.45f, -1.91f, 2.62f, -1.64f)
                curveToRelative(0.89f, 0.21f, 1.53f, 0.93f, 1.67f, 1.78f)
                curveToRelative(2.64f, 16.2f, -1.35f, 32.07f, -10.06f, 44.71f)
                curveToRelative(-8.67f, 12.58f, -22.03f, 21.97f, -38.18f, 25.29f)
                curveToRelative(-16.62f, 3.42f, -33.05f, -0.22f, -46.18f, -8.86f)
                curveTo(14.52f, 104.1f, 4.69f, 90.45f, 1.27f, 73.83f)
                curveTo(-2.07f, 57.6f, 1.32f, 41.55f, 9.53f, 28.58f)
                curveTo(17.78f, 15.57f, 30.88f, 5.64f, 46.91f, 1.75f)
                curveToRelative(0.31f, -0.08f, 0.67f, -0.16f, 1.06f, -0.25f)
                lineToRelative(0.01f, 0f)
                lineToRelative(0f, 0f)
                lineTo(49.06f, 1.27f)
                lineTo(49.06f, 1.27f)
                close()
            }
        }.build()

        return _MoonIcon!!
    }

@Suppress("ObjectPropertyName")
private var _MoonIcon: ImageVector? = null
