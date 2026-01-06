package io.github.themeanimator.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RuntimeThemeProvider(
    initial: Theme,
) : ThemeProvider {
    private val _currentTheme = MutableStateFlow(initial)
    override val currentTheme = _currentTheme.asStateFlow()

    override suspend fun updateTheme(theme: Theme) {
        _currentTheme.value = theme
    }

    companion object {

        val Saver: Saver<RuntimeThemeProvider, *> = Saver(
            save = { it.currentTheme.value.ordinal },
            restore = { RuntimeThemeProvider(Theme.fromOrdinal(it)) }
        )
    }
}

@Composable
fun rememberRuntimeThemeProvider(initial: Theme = Theme.System): RuntimeThemeProvider {
    return rememberSaveable(saver = RuntimeThemeProvider.Saver) {
        RuntimeThemeProvider(initial)
    }
}