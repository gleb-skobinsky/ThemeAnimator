package io.github.themeanimator.layout

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize

@Immutable
internal data class ButtonPosition(
    val offset: DpOffset,
    val size: DpSize,
)