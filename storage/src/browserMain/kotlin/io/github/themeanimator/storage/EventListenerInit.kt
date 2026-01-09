package io.github.themeanimator.storage

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

/**
 * Creates a JavaScript-compatible event listener from a Kotlin lambda function.
 *
 * This function bridges Kotlin's lambda syntax with JavaScript's EventListener interface,
 * enabling reactive listening to DOM events within the browser environment.
 *
 * @param listener The Kotlin lambda function to invoke when the event occurs.
 * @return An EventListener object that can be registered with DOM elements.
 */
@OptIn(ExperimentalWasmJsInterop::class)
internal fun createEventListener(listener: (Event) -> Unit): EventListener = js("({handleEvent: listener})")