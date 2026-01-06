package io.github.themeanimator.storage

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createAndroidDataStore(
    context: Context,
    preferencesFileName: String,
): DataStore<Preferences> = createDataStore(
    context.filesDir.resolve(preferencesFileName).absolutePath
)

@Composable
internal actual fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage {
    val context = LocalContext.current.applicationContext
    return remember {
        DataStoreStorage(
            internalStore = createAndroidDataStore(context, preferencesFileName),
            preferencesKey = preferencesKey
        )
    }
}