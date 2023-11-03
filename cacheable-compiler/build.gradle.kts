plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
    alias(libs.plugins.maven.publish)
}

dependencies {
    compileOnly(libs.kotlin.compiler.embeddable)

    ksp(libs.auto.service.ksp)
    compileOnly(libs.auto.service.annotations)
}
