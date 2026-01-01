package io.github.themeanimator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon

@Immutable
sealed interface ThemeSwitchIcon {

    @Composable
    fun Icon(
        isDark: Boolean,
        tint: Color,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
    )

    data class Vector(
        val imageVector: ImageVector,
    ) : ThemeSwitchIcon {

        @Composable
        override fun Icon(
            isDark: Boolean,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }

    data class DuoVector(
        val darkVector: ImageVector,
        val lightVector: ImageVector,
    ) : ThemeSwitchIcon {

        @Composable
        override fun Icon(
            isDark: Boolean,
            tint: Color,
            modifier: Modifier,
            contentDescription: String?,
        ) {
            Icon(
                imageVector = if (isDark) darkVector else lightVector,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }
}