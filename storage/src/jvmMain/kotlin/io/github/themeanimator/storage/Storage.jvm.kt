package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createJvmDataStore(
    preferencesFileName: String,
    jvmChildDirectory: String,
): DataStore<Preferences> = createDataStore(
    path = run {
        val baseDir = File(
            System.getProperty("user.home"),
            jvmChildDirectory
        )
        baseDir.mkdirs()
        File(baseDir, preferencesFileName).absolutePath
    }
)

@Composable
internal actual fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage {
    return remember {
        DataStoreStorage(
            internalStore = createJvmDataStore(preferencesFileName, jvmChildDirectory),
            preferencesKey = preferencesKey
        )
    }
}