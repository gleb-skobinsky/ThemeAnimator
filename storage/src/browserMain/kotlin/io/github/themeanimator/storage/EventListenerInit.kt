package io.github.themeanimator.storage

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalWasmJsInterop::class)
internal fun createEventListener(listener: (Event) -> Unit): EventListener = js("({handleEvent: listener})")