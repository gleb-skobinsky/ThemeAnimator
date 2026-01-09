package io.github.themeanimator.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath

/**
 * Creates a [DataStore] for preferences using the specified file path.
 *
 * This function initializes a DataStore that persists preferences to a file at the
 * given path. The DataStore uses protocol buffers for efficient serialization.
 *
 * @param path The absolute file path where preferences should be stored.
 * @return A configured [DataStore] instance for preferences.
 */
internal fun createDataStore(path: String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { path.toPath() }
    )

/**
 * A [Storage] implementation that uses Jetpack DataStore for theme persistence.
 *
 * This implementation provides reactive theme storage using Jetpack DataStore,
 * which offers type-safe, asynchronous data storage with a Flow-based API.
 * It's suitable for Android and other platforms that support DataStore.
 *
 * @param internalStore The [DataStore] instance used for persistence operations.
 * @param preferencesKey The string key used to identify the theme preference within the store.
 */
internal class DataStoreStorage(
    private val internalStore: DataStore<Preferences>,
    preferencesKey: String,
) : Storage {

    private val themeKey = intPreferencesKey(preferencesKey)

    override val rawTheme: Flow<Int> = internalStore.data.map {
        it[themeKey] ?: 0
    }.distinctUntilChanged()

    override suspend fun setRawTheme(theme: Int) {
        internalStore.edit {
            it[themeKey] = theme
        }
    }

    override fun getRawTheme(): Int {
        return runBlocking { internalStore.data.first()[themeKey] ?: 0 }
    }
}