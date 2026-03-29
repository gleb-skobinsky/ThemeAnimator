package io.github.themeanimator.button

@Suppress("ExperimentalAnnotationRetention")
@RequiresOptIn(
    message = "Experimental theme switching API. This API may change or be removed without notice."
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.TYPEALIAS,
    AnnotationTarget.CONSTRUCTOR
)
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalThemeSwitchApi