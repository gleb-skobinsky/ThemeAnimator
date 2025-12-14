package io.github.themeanimator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

internal enum class RecordStatus {
    Initial,
    RecordRequested,
    Recorded
}

@Stable
class ThemeAnimationState(
    initialIsDark: Boolean,
    private val coroutineScope: CoroutineScope,
    internal val animationSpec: AnimationSpec<Float>,
    internal val format: ThemeAnimationFormat,
    internal val interactionSource: MutableInteractionSource,
) {
    var isDark: Boolean by mutableStateOf(initialIsDark)
        private set

    internal val requestRecord = MutableStateFlow(RecordStatus.Initial)

    fun toggleTheme() {
        coroutineScope.launch {
            requestRecord.value = RecordStatus.RecordRequested
            requestRecord.firstOrNull { it == RecordStatus.Recorded }
            isDark = !isDark
        }
    }
}

@Composable
fun rememberThemeAnimationState(
    animationSpec: AnimationSpec<Float> = tween(300),
    initialIsDark: Boolean = isSystemInDarkTheme(),
    format: ThemeAnimationFormat = ThemeAnimationFormat.Sliding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
): ThemeAnimationState {
    val coroutineScope = rememberCoroutineScope()
    return remember(
        coroutineScope,
        animationSpec,
        format
    ) {
        ThemeAnimationState(
            initialIsDark = initialIsDark,
            coroutineScope = coroutineScope,
            animationSpec = animationSpec,
            format = format,
            interactionSource = interactionSource
        )
    }
}