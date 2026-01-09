package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

/**
 * Creates a JVM-specific [DataStore] for preferences storage.
 *
 * This function initializes a DataStore that stores preferences in a subdirectory
 * within the user's home directory on JVM platforms (desktop applications).
 * The directory structure follows common conventions for application data storage
 * on desktop systems.
 *
 * @param preferencesFileName The name of the preferences file to create.
 * @param jvmChildDirectory The name of the subdirectory to create within the user's home directory.
 * @return A configured [DataStore] instance for JVM platforms.
 */
internal fun createJvmDataStore(
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