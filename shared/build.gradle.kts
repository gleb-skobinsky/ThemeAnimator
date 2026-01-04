import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.mavenPublish)
    id("signing")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosArm64()
    iosSimulatorArm64()

    jvm()

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        androidMain.dependencies {
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.components.resources)
            implementation(libs.androidx.collection)
            implementation(libs.compottie)
            implementation(libs.compottie.lite)
            implementation(libs.compottie.dot)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "io.github.themeanimator"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        val targetSdkVersion = libs.versions.android.targetSdk.get().toInt()
        testOptions.targetSdk = targetSdkVersion
        lint.targetSdk = targetSdkVersion
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.ui.tooling)
}

fun loadProperties(): Properties? {
    val keystorePropertiesFile = rootProject.file("local.properties")
    if (!keystorePropertiesFile.exists()) {
        return null
    }
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    return keystoreProperties
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates("io.github.gleb-skobinsky", "themeanimator", "0.0.4")

    pom {
        name = "ThemeAnimator"
        description = "Compose multiplarform theme animation library"
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
