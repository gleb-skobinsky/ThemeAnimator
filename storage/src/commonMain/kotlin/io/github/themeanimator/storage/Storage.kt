package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

internal interface Storage {
    val rawTheme: Flow<Int>

    suspend fun setRawTheme(theme: Int)

    fun getRawTheme(): Int

    companion object {
        const val THEME_KEY = "STORE_KEY_THEME"
    }
}

@Composable
internal expect fun getThemeStorage(): Storage