package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

/**
 * Internal interface for theme storage operations across different platforms.
 *
 * This interface abstracts theme persistence mechanisms, allowing different platforms
 * to implement storage using their native solutions (SharedPreferences on Android,
 * UserDefaults on iOS, localStorage on Web, etc.).
 */
internal interface Storage {

    /**
     * A [Flow] that emits the raw theme value whenever it changes.
     *
     * The flow provides reactive updates to theme changes, enabling UI components
     * to respond to theme updates from any source. The raw theme is represented
     * as an integer corresponding to [io.github.themeanimator.theme.Theme.ordinal].
     */
    val rawTheme: Flow<Int>

    /**
     * Persists the raw theme value to storage.
     *
     * @param theme The theme ordinal value to store.
     */
    suspend fun setRawTheme(theme: Int)

    /**
     * Retrieves the current raw theme value from storage synchronously.
     *
     * This method provides immediate access to the stored theme value without
     * requiring suspension. It's used for initialization and cases where the
     * current value is needed immediately.
     *
     * @return The stored theme ordinal value, or a default value if none exists.
     */
    fun getRawTheme(): Int
}

/**
 * Creates a platform-specific [Storage] implementation for theme persistence.
 *
 * This expect function is implemented differently on each platform to use the
 * most appropriate storage mechanism available on that platform.
 *
 * @param preferencesFileName The name of the preferences file or storage key.
 * @param preferencesKey The key used to store the theme value within the storage.
 * @param jvmChildDirectory The subdirectory name for JVM platforms (used in user home directory).
 * @return A platform-specific [Storage] implementation.
 */
@Composable
internal expect fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage