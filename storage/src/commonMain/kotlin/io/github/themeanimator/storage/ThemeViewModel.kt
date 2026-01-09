package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.themeanimator.theme.Theme
import io.github.themeanimator.theme.ThemeProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KClass

internal class ThemeViewModel(
    private val storage: Storage,
) : ViewModel(), ThemeProvider {
    override val currentTheme: StateFlow<Theme> = storage.rawTheme.map {
        Theme.fromOrdinal(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Theme.fromOrdinal(storage.getRawTheme())
    )

    override suspend fun updateTheme(theme: Theme) {
        storage.setRawTheme(theme.ordinal)
    }
}

/**
 * Creates and provides a [ThemeProvider] backed by persistent storage using Android's ViewModel.
 *
 * This composable function creates a ViewModel-based [ThemeProvider] that persists theme
 * changes across app launches and survives configuration changes. It uses platform-specific
 * storage mechanisms to ensure theme preferences are maintained between sessions.
 *
 * The returned provider integrates with Jetpack Compose's ViewModel system, providing
 * automatic cleanup and lifecycle management while maintaining theme state consistency.
 *
 * @param preferencesFileName The name of the preferences file used for storage.
 *                           Defaults to "theme_animator.preferences_pb".
 * @param preferencesKey The key used to store the theme value within the preferences.
 *                      Defaults to "STORE_KEY_THEME".
 * @param jvmChildDirectory The subdirectory name for JVM platforms, created within the user's
 *                         home directory. Defaults to ".myapp". This parameter is ignored on
 *                         other platforms.
 * @return A [ThemeProvider] instance that persists theme changes and survives configuration changes.
 */
@Composable
@Suppress("UNCHECKED_CAST")
fun themeViewModel(
    preferencesFileName: String = "theme_animator.preferences_pb",
    preferencesKey: String = "STORE_KEY_THEME",
    jvmChildDirectory: String = ".myapp",
): ThemeProvider {
    val storage = getThemeStorage(
        preferencesFileName = preferencesFileName,
        preferencesKey = preferencesKey,
        jvmChildDirectory = jvmChildDirectory
    )
    return viewModel<ThemeViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: KClass<T>,
                extras: CreationExtras
            ): T {
                return ThemeViewModel(storage) as T
            }
        }
    )
}