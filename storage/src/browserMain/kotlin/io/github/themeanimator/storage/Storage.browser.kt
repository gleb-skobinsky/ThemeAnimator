package io.github.themeanimator.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.w3c.dom.CustomEvent
import org.w3c.dom.CustomEventInit
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.unsafeCast
import org.w3c.dom.Storage as WebStorage

@OptIn(ExperimentalWasmJsInterop::class)
internal class JsStorage(
    private val preferencesKey: String,
) : Storage {
    override val rawTheme: Flow<Int> = observeKey(preferencesKey).map {
        it.toIntOrNull() ?: 0
    }.distinctUntilChanged()

    override fun getRawTheme(): Int {
        return localStorage[preferencesKey]?.toIntOrNull() ?: 0
    }

    override suspend fun setRawTheme(theme: Int) {
        localStorage.setValue(preferencesKey, theme.toString())
    }


    private fun WebStorage.setValue(key: String, value: String) {
        document.dispatchEvent(
            CustomEvent(
                type = STORAGE_EVENT_KEY,
                eventInitDict = CustomEventInit(
                    detail = EventDetail(key, value)
                )
            )
        )
        set(key, value)
    }

    private fun observeKey(key: String): Flow<String> {
        return callbackFlow {
            val listener = createEventListener { event ->
                val detail = (event as? CustomEvent)?.detail ?: return@createEventListener
                detail.unsafeCast<EventDetail>().let { detail ->
                    if (detail.key == key) {
                        launch {
                            send(detail.value)
                        }
                    }
                }
            }
            document.addEventListener(STORAGE_EVENT_KEY, listener)
            awaitClose {
                document.removeEventListener(STORAGE_EVENT_KEY, listener)
            }
        }
    }

    private companion object {
        const val STORAGE_EVENT_KEY = "localStorageUpdate"
    }
}

@Composable
internal actual fun getThemeStorage(
    preferencesFileName: String,
    preferencesKey: String,
    jvmChildDirectory: String,
): Storage {
    return remember { JsStorage(preferencesKey) }
}