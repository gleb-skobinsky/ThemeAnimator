package org.violet.violetapp.secureStorage

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.themeanimator.storage.Storage
import io.github.themeanimator.storage.getThemeStorage
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

@Composable
@Suppress("UNCHECKED_CAST")
fun themeViewModel(): ThemeProvider {
    val storage = getThemeStorage()
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