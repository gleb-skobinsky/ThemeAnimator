plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
}

dokka {
    dokkaPublications {
        html {
            outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        }
    }
}

dependencies {
    dokka(projects.core)
    dokka(projects.storage)
}


subprojects {
    apply(plugin = "org.jetbrains.dokka")
}
