package com.moriatsushi.cacheable.compiler

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import com.moriatsushi.cacheable.compiler.factory.IrCacheStoreFieldFactory
import com.moriatsushi.cacheable.compiler.factory.IrCacheableExpressionBodyFactory
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

class CacheableIrGenerationExtension : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(createCacheableIrElementTransformer(pluginContext))
    }

    private fun createCacheableIrElementTransformer(
        pluginContext: IrPluginContext,
    ): CacheableIrElementTransformer {
        val cacheableDeclarations = CacheableDeclarations.find(pluginContext)
            ?: error("Please check the dependency for kotlin-cacheable")
        val irCacheStoreFieldFactory = IrCacheStoreFieldFactory(
            pluginContext.irFactory,
            cacheableDeclarations,
        )
        val irCacheableExpressionBodyFactory = IrCacheableExpressionBodyFactory(
            pluginContext.irFactory,
            cacheableDeclarations,
        )
        return CacheableIrElementTransformer(
            irCacheStoreFieldFactory = irCacheStoreFieldFactory,
            irCacheableExpressionBodyFactory = irCacheableExpressionBodyFactory,
        )
    }
}
