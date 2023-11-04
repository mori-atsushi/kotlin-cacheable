import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.moriatsushi.cacheable") version "0.0.1"
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
        browser()
    }
    @Suppress("OPT_IN_USAGE")
    wasmJs {
        binaries.executable()
        nodejs()
        browser()
    }
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

    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()

    mingwX64()
    linuxX64()
    linuxArm64()

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(project(":cacheable-core"))
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.withType(KotlinCompile::class).all {
    dependsOn(":cacheable-compiler:publishToMavenLocal")
}