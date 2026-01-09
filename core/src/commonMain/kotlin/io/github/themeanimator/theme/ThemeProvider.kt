package io.github.themeanimator.theme

import kotlinx.coroutines.flow.StateFlow

/**
 * An interface that provides theme management capabilities.
 *
 * Implementations of this interface are responsible for managing the current theme state
 * and providing the ability to update the theme. This abstraction allows for different
 * storage backends (in-memory, persistent storage, etc.) while maintaining a consistent API.
 */
interface ThemeProvider {

    /**
     * A [StateFlow] that emits the current theme whenever it changes.
     *
     * This property provides reactive updates to theme changes, allowing UI components
     * to automatically respond to theme transitions. The flow emits the initial theme
     * value immediately upon collection.
     */
    val currentTheme: StateFlow<Theme>

    /**
     * Updates the current theme to the specified value.
     *
     * This is a suspending function as it may perform I/O operations when persisting
     * the theme change to storage. The implementation should update both the internal
     * state and any persistent storage.
     *
     * @param theme The new theme to apply.
     */
    suspend fun updateTheme(theme: Theme)
}