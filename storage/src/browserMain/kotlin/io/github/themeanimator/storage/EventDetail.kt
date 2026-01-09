package io.github.themeanimator.storage

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

/**
 * JavaScript event detail interface for storage change notifications.
 *
 * This interface represents the detail object passed with custom storage events,
 * containing information about which storage key was modified and its new value.
 */
@OptIn(ExperimentalWasmJsInterop::class)
external interface EventDetail : JsAny {

    /**
     * The storage key that was modified.
     */
    val key: String

    /**
     * The new value associated with the key.
     */
    val value: String
}

/**
 * Creates an [EventDetail] instance with the specified key and value.
 *
 * This function provides a convenient way to construct EventDetail objects
 * for storage change notifications in the browser environment.
 *
 * @param key The storage key that was modified.
 * @param value The new value associated with the key.
 * @return An EventDetail instance containing the provided key-value pair.
 */
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun(
    """
    function(key, value) {
        return { key: key, value: value };
    }
"""
)
external fun EventDetail(key: String, value: String): EventDetail