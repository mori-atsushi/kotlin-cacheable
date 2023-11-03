package com.moriatsushi.cacheable.compiler

import com.moriatsushi.cacheable.compiler.factory.IrCacheStoreFieldFactory
import com.moriatsushi.cacheable.compiler.factory.IrCacheableExpressionBodyFactory
import com.moriatsushi.cacheable.compiler.resolver.ClassResolver
import com.moriatsushi.cacheable.compiler.resolver.TypeResolver
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
        val classResolver = ClassResolver(pluginContext)
        val typeResolver = TypeResolver(classResolver)
        val irCacheStoreFieldFactory = IrCacheStoreFieldFactory(
            pluginContext.irFactory,
            classResolver,
            typeResolver,
        )
        val irCacheableExpressionBodyFactory = IrCacheableExpressionBodyFactory(pluginContext)
        return CacheableIrElementTransformer(
            irCacheStoreFieldFactory = irCacheStoreFieldFactory,
            irCacheableExpressionBodyFactory = irCacheableExpressionBodyFactory,
        )
    }
}
