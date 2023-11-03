package com.moriatsushi.cacheable.gradle.plugin

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class CacheableCompilerPluginSupportPlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> =
        kotlinCompilation.target.project.provider { emptyList() }

    override fun getCompilerPluginId(): String = "com.moriatsushi.cacheable.compiler"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "com.moriatsushi.cacheable",
        artifactId = "cacheable-compiler",
        version = "0.0.1"
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true
}
