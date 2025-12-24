package io.github.themeanimator

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val SunIcon: ImageVector
    get() {
        if (SunWarmIcon != null) {
            return SunWarmIcon!!
        }
        SunWarmIcon = ImageVector.Builder(
            name = "SunWarmIcon",
            defaultWidth = 122.88.dp,
            defaultHeight = 122.88.dp,
            viewportWidth = 122.88f,
            viewportHeight = 122.88f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(30f, 13.21f)
                arcTo(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, 36.8f, 9.27f)
                lineTo(41.86f, 18f)
                arcTo(3.94f, 3.94f, 0f, isMoreThanHalf = true, isPositiveArc = true, 35.05f, 22f)
                lineTo(30f, 13.21f)
                close()
                moveTo(61.45f, 26.21f)
                arcTo(35.23f, 35.23f, 0f, isMoreThanHalf = true, isPositiveArc = true, 36.52f, 36.52f)
                arcTo(35.13f, 35.13f, 0f, isMoreThanHalf = false, isPositiveArc = true, 61.44f, 26.2f)
                close()
                moveTo(58.31f, 4f)
                arcTo(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, 66.2f, 4f)
                lineTo(66.2f, 14.06f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7.89f, 0f)
                lineTo(58.31f, 4f)
                close()
                moveTo(87.49f, 10.1f)
                arcTo(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, 94.3f, 14f)
                lineToRelative(-5.06f, 8.76f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6.81f, -3.92f)
                lineToRelative(5.06f, -8.75f)
                close()
                moveTo(109.67f, 30f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, 3.94f, 6.81f)
                lineToRelative(-8.75f, 5.06f)
                arcToRelative(3.94f, 3.94f, 0f, isMoreThanHalf = true, isPositiveArc = true, -4f, -6.81f)
                lineTo(109.67f, 30f)
                close()
                moveTo(118.93f, 58.32f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 7.89f)
                lineTo(108.82f, 66.21f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -7.89f)
                close()
                moveTo(112.78f, 87.5f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, -3.91f, 6.81f)
                lineToRelative(-8.76f, -5.06f)
                arcTo(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, 104f, 82.43f)
                lineToRelative(8.75f, 5.06f)
                close()
                moveTo(92.89f, 109.67f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6.81f, 3.94f)
                lineTo(81f, 104.86f)
                arcToRelative(3.94f, 3.94f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6.81f, -4f)
                lineToRelative(5.06f, 8.76f)
                close()
                moveTo(64.57f, 118.93f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, -7.89f, 0f)
                lineTo(56.68f, 108.82f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, 7.89f, 0f)
                verticalLineToRelative(10.11f)
                close()
                moveTo(35.39f, 112.78f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = false, isPositiveArc = true, -6.81f, -3.91f)
                lineToRelative(5.06f, -8.76f)
                arcTo(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, 40.45f, 104f)
                lineToRelative(-5.06f, 8.75f)
                close()
                moveTo(13.21f, 92.89f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, -3.94f, -6.81f)
                lineTo(18f, 81f)
                arcTo(3.94f, 3.94f, 0f, isMoreThanHalf = true, isPositiveArc = true, 22f, 87.83f)
                lineToRelative(-8.76f, 5.06f)
                close()
                moveTo(4f, 64.57f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -7.89f)
                lineTo(14.06f, 56.68f)
                arcToRelative(3.95f, 3.95f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 7.89f)
                close()
                moveTo(10.1f, 35.39f)
                arcTo(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, 14f, 28.58f)
                lineToRelative(8.76f, 5.06f)
                arcToRelative(3.93f, 3.93f, 0f, isMoreThanHalf = true, isPositiveArc = true, -3.92f, 6.81f)
                lineTo(10.1f, 35.39f)
                close()
            }
        }.build()

        return SunWarmIcon!!
    }

private var SunWarmIcon: ImageVector? = null
