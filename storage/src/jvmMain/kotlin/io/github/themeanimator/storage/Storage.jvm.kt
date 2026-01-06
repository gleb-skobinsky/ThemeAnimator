package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

fun createDataStore(): DataStore<Preferences> = createDataStore(
    path = run {
        val baseDir = File(
            System.getProperty("user.home"),
            ".myapp"
        )
        baseDir.mkdirs()
        File(baseDir, dataStoreFileName).absolutePath
    }
)

@Composable
internal actual fun getThemeStorage(): Storage {
    return remember { DataStoreStorage(createDataStore()) }
}