import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
}

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

    iosArm64()
    iosSimulatorArm64()

    jvm()

    @Suppress("Unused")
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(libs.coroutines)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
        }
        val dataStoreMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.datastore.settings)
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
        val jvmMain by getting {
            dependsOn(dataStoreMain)
        }
        androidMain {
            dependsOn(dataStoreMain)
        }
        val browserMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.multiplatform.settings)
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
