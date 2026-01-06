package io.github.themeanimator.storage

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        context.filesDir.resolve(dataStoreFileName).absolutePath
    )

@Composable
internal actual fun getThemeStorage(): Storage {
    val context = LocalContext.current.applicationContext
    return remember {
        DataStoreStorage(createDataStore(context))
    }
}