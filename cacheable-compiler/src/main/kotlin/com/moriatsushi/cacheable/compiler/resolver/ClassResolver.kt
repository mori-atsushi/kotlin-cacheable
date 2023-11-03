package com.moriatsushi.cacheable.compiler.resolver

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class ClassResolver(pluginContext: IrPluginContext) {
    val irCacheStoreClass: IrClassSymbol =
        pluginContext.referenceClass(cacheStoreId) ?: error("CacheStore class not found")

    companion object {
        private val internalPackageName = FqName("com.moriatsushi.cacheable.internal")

        private val cacheStoreId = ClassId(internalPackageName, Name.identifier("CacheStore"))
    }
}
