import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.mavenPublish)
    id("signing")
}

val libraryVersion: String = libs.versions.themeanimator.version.get()

group = "io.github.gleb-skobinsky"
version = libraryVersion

kotlin {
    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("Unused")
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(libs.coroutines)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.stately.collections)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
        }
        val dataStoreMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.androidx.datastore)
            }
        }
        val iosMain by creating {
            dependsOn(dataStoreMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val jvmMain by getting {
            dependsOn(dataStoreMain)
        }
        androidMain {
            dependsOn(dataStoreMain)
        }
        val browserMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.kotlinx.browser)
            }
        }
        val jsMain by getting {
            dependsOn(browserMain)
        }
        val wasmJsMain by getting {
            dependsOn(browserMain)
        }
    }
}

android {
    namespace = "io.github.themeanimator.storage"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(
        groupId = "io.github.gleb-skobinsky",
        artifactId = "themeanimator-storage",
        version = libs.versions.themeanimator.version.get()
    )

    pom {
        name = "ThemeAnimator"
        description = "Compose multiplarform theme animation library storage extension"
        inceptionYear = "2025"
        url = "https://github.com/gleb-skobinsky/ThemeAnimator"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "skobinsky"
                name = "Gleb Gutnik"
                url = "https://github.com/gleb-skobinsky"
            }
        }
        scm {
            url = "https://github.com/gleb-skobinsky/ThemeAnimator"
            connection = "scm:git:git://github.com/gleb-skobinsky/ThemeAnimator.git"
            developerConnection =
                "scm:git:ssh://git@github.com/gleb-skobinsky/ThemeAnimator.git"
        }
    }
}
