This is a Compose Multiplatform library providing a theme transitioning animation.
 
### Installation and quick-start

In order to install the library with Gradle, be sure to use the mavenCentral() repository in your project.

Then simply use in dependencies:

```kotlin
implementation("io.github.gleb-skobinsky:themeanimator:0.0.11")
```

### Usage

In order to animate your screen responsible for dark/light theme switching, wrap it with the `ThemeAnimationScope` composable:

```kotlin
@Composable
fun App() {
    val animationState = rememberThemeAnimationState(
        format = ThemeAnimationFormat.CircularAroundPress
    )
    val theme = if (animationState.uiTheme.isDark()) darkColorScheme() else lightColorScheme()
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
                            animationState = animationState
                        )
                    }
                }
            ) { contentPadding ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxSize()
                        .padding(contentPadding)
                )
            }
        }
    }
}
```

The `ThemeAnimationState` is the primary object to manage and trigger animations.
It should be created with the `rememberThemeAnimationState` utility function. 

Please note that your own theme object must be updated based on the `ThemeAnimationState.uiTheme` field. This is a known limitation, but it is essential for the animation to work.

### Theme animation formats

Theme animations can be customized with an implementation of `ThemeAnimationFormat`. Four standard formats are provided out of the box:
- Crossfade
- Sliding
- Circular reveal
- Circular reveal around press

An example of the latter animation:

https://github.com/user-attachments/assets/8d737094-1e41-4769-9356-e3d74063e492


Please note that if your animation interacts with the position of the pressed button, the standard `ThemeSwitchButton` should be used. Otherwise, you want to implement your own theme switch, notify the state with the `ThemeAnimationState.updateButtonPosition` method.

### Theme animation button

The theme animation button is provided with the `ThemeSwitchButton` Composable. 5 types of button icons are provided. 
For animations between the dark and light vector icons, you can utilize Lottie animations:
```kotlin
rememberLottieIcon(
    startProgress = 1f,
    endProgress = 0.5f,
    animationSpec = animationSpec
) {
    JsonString(Res.readBytes("files/anim.json").decodeToString())
}
```
### Persistent theme provider
In most real-world production use-cases, a persistent local or remote storage is needed to manage the selected theme. Such storage can be provided by implementing and passing a custom `ThemeProvider`. 
If you opt-in to use a ready-made solution, though, you can use the `themeViewModel` from an additional library:
```
// In your Gradle files
implementation(io.github.gleb-skobinsky:themeanimator-storage:0.0.11)

val animationState = rememberThemeAnimationState(
    themeProvider = themeViewModel(),
    animationSpec = animationSpec,
    format = ThemeAnimationFormat.CircularAroundPress
)
```
**What's under the hood?**
The theme storage library internally uses Androidx Datastore for all platforms except WASM and JS (in the browser, `localStorage` is used instead). Thanks to this, the theme is persisted across app relaunches.
