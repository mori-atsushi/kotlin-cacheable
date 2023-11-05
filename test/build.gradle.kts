import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.moriatsushi.cacheable") version "0.0.2"
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
        browser()
    }
    // TODO: Support Kotlin/WASM
    /*
    @Suppress("OPT_IN_USAGE")
    wasmJs {
        binaries.executable()
        nodejs()
        browser()
    }
    */
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    watchosArm64()
    watchosX64()
    watchosSimulatorArm64()
    watchosDeviceArm64()
    tvosArm64()
    tvosX64()
    tvosSimulatorArm64()

    mingwX64()
    linuxX64()
    linuxArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":cacheable-core"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

tasks.withType(KotlinCompile::class).all {
    dependsOn(":cacheable-compiler:publishToMavenLocal")
}
