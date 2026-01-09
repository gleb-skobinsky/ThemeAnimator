package io.github.themeanimator.storage

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

@OptIn(ExperimentalWasmJsInterop::class)
external interface EventDetail : JsAny {
    val key: String
    val value: String
}

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun(
    """
    function(key, value) {
        return { key: key, value: value };
    }
"""
)
external fun EventDetail(key: String, value: String): EventDetail