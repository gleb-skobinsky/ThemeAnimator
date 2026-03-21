package io.github.themeanimator

internal enum class RecordStatus(
    val isAnimating: Boolean,
) {
    Initial(false),
    RecordRequested(true),
    Recorded(true),
    PrepareForAnimation(true),
}