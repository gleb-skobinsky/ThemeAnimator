package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Composable
internal actual fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage {
    return remember {
        DataStoreStorage(
            internalStore = createIosDataStore(preferencesFileName),
            preferencesKey = preferencesKey
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
fun createIosDataStore(
    preferencesFileName: String,
): DataStore<Preferences> = createDataStore(
    path = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )!!.path + "/$preferencesFileName"
)