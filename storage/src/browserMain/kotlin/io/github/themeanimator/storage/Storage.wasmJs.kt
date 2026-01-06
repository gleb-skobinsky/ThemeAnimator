package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.w3c.dom.get
import org.w3c.dom.set

internal class JsStorage(
    private val preferencesKey: String,
) : Storage {

    private val _rawTheme = MutableStateFlow(getRawTheme())
    override val rawTheme = _rawTheme.asStateFlow()

    override fun getRawTheme(): Int {
        return localStorage[preferencesKey]?.toIntOrNull() ?: 0
    }

    override suspend fun setRawTheme(theme: Int) {
        _rawTheme.value = theme
        localStorage[preferencesKey] = theme.toString()
    }
}

@Composable
internal actual fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage {
    return remember { JsStorage(preferencesKey) }
}