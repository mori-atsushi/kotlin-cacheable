plugins {
    `java-gradle-plugin`
    kotlin("jvm")
    alias(libs.plugins.maven.publish)
}

dependencies {
    implementation(kotlin("gradle-plugin-api"))
}

gradlePlugin {
    plugins {
        create("cacheable") {
            id = "com.moriatsushi.cacheable"
            implementationClass =
                "com.moriatsushi.cacheable.gradle.plugin.CacheableCompilerPluginSupportPlugin"
        }
    }
}
