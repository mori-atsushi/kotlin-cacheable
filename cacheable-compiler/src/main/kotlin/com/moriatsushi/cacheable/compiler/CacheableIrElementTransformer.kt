package com.moriatsushi.cacheable.compiler

import com.moriatsushi.cacheable.compiler.factory.IrCacheStoreFieldFactory
import com.moriatsushi.cacheable.compiler.factory.IrCacheableFunctionFactory
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationContainer
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class CacheableIrElementTransformer(
    private val irCacheStoreFieldFactory: IrCacheStoreFieldFactory,
    private val irCacheableFunctionFactory: IrCacheableFunctionFactory,
) : IrElementTransformerVoid() {
    override fun visitFunction(declaration: IrFunction): IrStatement {
        if (!declaration.hasAnnotation(cacheableAnnotation) || declaration !is IrSimpleFunction) {
            return declaration
        }

        val parent = (declaration.parent as? IrDeclarationContainer)
            ?: error("Unexpected function parent type")

        val irCacheStoreField = irCacheStoreFieldFactory.create(declaration)
        parent.declarations.add(irCacheStoreField)

        val cacheableFunction = irCacheableFunctionFactory.create(declaration, irCacheStoreField)
        parent.declarations.add(cacheableFunction)

        declaration.name = Name.identifier("_${declaration.name}_actual")

        return declaration
    }

    companion object {
        val cacheableAnnotation = FqName("com.moriatsushi.cacheable.Cacheable")
    }
}
