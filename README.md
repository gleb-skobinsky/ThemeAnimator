This is a Compose Multiplatform library providing a theme transitioning animation.
 
### Installation and quick-start

In order to install the library with Gradle, be sure to use the mavenCentral() repository in your project.

Then simply use in dependencies:

```kotlin
implementation("io.github.gleb-skobinsky:themeanimator:0.0.4")
```

### Usage

In order to animate your screen responsible for dark/light theme switching, wrap it with the `ThemeAnimationScope` composable:

```kotlin
val animationState = rememberThemeAnimationState(
    format = ThemeAnimationFormat.CircularAroundPress
)
val theme = if (animationState.isDark) darkColorScheme() else lightColorScheme()
ThemeAnimationScope(
    theme = theme,
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
                        animationState = animationState
                    )
                }
            }
        ) { contentPadding ->
            // Your screen here
        }
    }
}
```

The `ThemeAnimationState` is the primary object to manage and trigger animations.
It should be created with the `rememberThemeAnimationState` utility function. 

Please note that your own theme object must be updated based on the `ThemeAnimationState.isDark` flag. This is a known limitation, but it is essential for the animation to work.

### Theme animation formats

Theme animations can be customized with an implementation of `ThemeAnimationFormat`. Four standard formats are provided out of the box:
- Crossfade
- Sliding
- Circular reveal
- Circular reveal around press

An example of the latter animation:

https://github.com/user-attachments/assets/8d737094-1e41-4769-9356-e3d74063e492


Please note that if your animation interacts with the position of the pressed button, the standard `ThemeSwitchButton` should be used. Otherwise, you want to implement your own theme switch, notify the state with the `ThemeAnimationState.updateButtonPosition` method.
