package io.github.themeanimator.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A runtime-only implementation of [ThemeProvider] that stores theme state in memory.
 *
 * This provider maintains theme state only during the application's runtime and does not
 * persist theme changes to any storage mechanism. The theme state is lost when the application
 * is terminated. It's suitable for temporary theme switching or when persistence is handled
 * by other components.
 *
 * @param initial The initial theme to start with.
 */
class RuntimeThemeProvider(
    initial: Theme,
) : ThemeProvider {
    private val _currentTheme = MutableStateFlow(initial)

    override val currentTheme = _currentTheme.asStateFlow()

    override suspend fun updateTheme(theme: Theme) {
        _currentTheme.value = theme
    }

    companion object {

        /**
         * A [Saver] for persisting and restoring [RuntimeThemeProvider] state.
         *
         * This saver enables the provider to survive configuration changes and process death
         * in Android by saving the current theme ordinal and restoring the provider with
         * the same theme state.
         */
        val Saver: Saver<RuntimeThemeProvider, *> = Saver(
            save = { it.currentTheme.value.ordinal },
            restore = { RuntimeThemeProvider(Theme.fromOrdinal(it)) }
        )
    }
}

/**
 * Creates and remembers a [RuntimeThemeProvider] with the specified initial theme.
 *
 * The provider is remembered across recompositions and configuration changes using
 * the built-in [RuntimeThemeProvider.Saver]. This ensures theme state consistency
 * during the component's lifecycle.
 *
 * @param initial The initial theme to start with. Defaults to [Theme.System].
 * @return A remembered [RuntimeThemeProvider] instance.
 */
@Composable
fun rememberRuntimeThemeProvider(initial: Theme = Theme.System): RuntimeThemeProvider {
    return rememberSaveable(saver = RuntimeThemeProvider.Saver) {
        RuntimeThemeProvider(initial)
    }
}