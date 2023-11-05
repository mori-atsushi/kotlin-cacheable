pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            // `kotlinx-atomicfu` is not published to Gradle Plugin Portal
            // https://github.com/Kotlin/kotlinx-atomicfu/issues/56
            if (requested.id.id == "kotlinx-atomicfu") {
                useModule("org.jetbrains.kotlinx:atomicfu-gradle-plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "cacheable"
include(":cacheable-compiler")
include(":cacheable-core")
include(":cacheable-gradle-plugin")
include(":test")
