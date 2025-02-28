pluginManagement {
    val kotlinVersion: String by settings
    val androidBuildToolsVersion: String by settings // Renamed for clarity

    plugins {
        kotlin("multiplatform").version(kotlinVersion) // Idiomatic Kotlin DSL
        kotlin("plugin.serialization").version(kotlinVersion) // Idiomatic Kotlin DSL
        id("org.jetbrains.dokka").version(kotlinVersion)
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) { // Using when for better readability
                "kotlin-multiplatform", "org.jetbrains.kotlin.jvm", "kotlin-android-extensions" -> {
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
                }
                "kotlinx-serialization" -> {
                    useModule("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
                }
                "com.android.library", "com.android.application" -> { // Added "com.android.application"
                    useModule("com.android.tools.build:gradle:$androidBuildToolsVersion")
                }
            }
        }
    }

    repositories {
        gradlePluginPortal() // Put the most specific repository first
        google()
        mavenCentral()
    }
}

rootProject.name = "spotify-api-kotlin"