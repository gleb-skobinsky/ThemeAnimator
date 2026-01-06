package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import io.github.themeanimator.storage.Storage.Companion.THEME_KEY

@OptIn(ExperimentalSettingsApi::class)
internal class JsStorage : Storage {
    private val settings = StorageSettings()

    private val _rawTheme = MutableStateFlow(getRawTheme())
    override val rawTheme: Flow<Int> = _rawTheme.asStateFlow()

    override fun getRawTheme(): Int {
        return settings.getIntOrNull(THEME_KEY) ?: 0
    }

    override suspend fun setRawTheme(theme: Int) {
        _rawTheme.value = theme
        settings[THEME_KEY] = theme
    }
}

@Composable
internal actual fun getThemeStorage(): Storage {
    return remember { JsStorage() }
}