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

fun createDataStore(path: String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { path.toPath() }
    )

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