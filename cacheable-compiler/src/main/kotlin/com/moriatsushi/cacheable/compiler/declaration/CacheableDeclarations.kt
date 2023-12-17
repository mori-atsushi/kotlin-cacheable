package com.moriatsushi.cacheable.compiler.declaration

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

class CacheableDeclarations(
    val cacheableAnnotationDeclaration: CacheableAnnotationDeclaration,
    val cacheStoreClassDeclaration: CacheStoreClassDeclaration,
    val suspendCacheStoreClassDeclaration: SuspendCacheStoreClassDeclaration,
) {
    companion object {
        fun find(pluginContext: IrPluginContext): CacheableDeclarations? {
            val cacheStoreClassDeclaration =
                CacheStoreClassDeclaration.find(pluginContext) ?: return null
            val suspendCacheStoreClassDeclaration =
                SuspendCacheStoreClassDeclaration.find(pluginContext) ?: return null

            return CacheableDeclarations(
                CacheableAnnotationDeclaration(),
                cacheStoreClassDeclaration,
                suspendCacheStoreClassDeclaration,
            )
        }
    }
}
