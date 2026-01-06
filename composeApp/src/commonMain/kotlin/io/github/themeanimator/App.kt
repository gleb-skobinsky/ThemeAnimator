package io.github.themeanimator

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.themeanimator.button.ThemeSwitchButton
import io.github.themeanimator.button.rememberLottieIconJson
import io.github.themeanimator.theme.isDark
import themeanimator.composeapp.generated.resources.Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val animationSpec = tween<Float>(700)
    val animationState = rememberThemeAnimationState(
        animationSpec = animationSpec,
        format = ThemeAnimationFormat.CircularAroundPress
    )
    val theme = if (animationState.uiTheme.isDark()) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    ThemeAnimationScope(
        state = animationState
    ) {
        MaterialTheme(
            colorScheme = theme
        ) {
            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier.statusBarsPadding().fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End
                    ) {
                        ThemeSwitchButton(
                            animationState = animationState,
                            buttonIcon = rememberLottieIconJson(
                                startProgress = 0.9f,
                                endProgress = 0.5f,
                                animationSpec = animationSpec
                            ) {
                                Res.readBytes("files/anim.json").decodeToString()
                            },
                            iconModifier = Modifier.padding(end = 12.dp),
                            iconSize = 40.dp,
                            iconScale = 2f
                        )
                    }
                }
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(
                        modifier = Modifier.padding(vertical = 12.dp),
                        onClick = {}
                    ) {
                        Text("Click me!")
                    }
                    repeat(3) {
                        Text(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod " +
                                    "tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
                                    "et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                                    "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt " +
                                    "ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
                                    "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."
                        )
                    }
                }
            }
        }
    }
}