package io.github.themeanimator.storage

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Creates an Android-specific [DataStore] for preferences storage.
 *
 * This function initializes a DataStore that stores preferences in the app's internal
 * storage directory on Android. The preferences file will be created in the context's
 * files directory, making it private to the application.
 *
 * @param context The Android context used to access the app's files directory.
 * @param preferencesFileName The name of the preferences file to create.
 * @return A configured [DataStore] instance for Android.
 */
internal fun createAndroidDataStore(
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