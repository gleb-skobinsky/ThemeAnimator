package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

internal interface Storage {
    val rawTheme: Flow<Int>

    suspend fun setRawTheme(theme: Int)

    fun getRawTheme(): Int
}

@Composable
internal expect fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage