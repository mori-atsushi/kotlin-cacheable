package com.moriatsushi.cacheable.compiler

import com.moriatsushi.cacheable.compiler.factory.IrCacheStoreFieldFactory
import com.moriatsushi.cacheable.compiler.factory.IrCacheableExpressionBodyFactory
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationContainer
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.FqName

class CacheableIrElementTransformer(
    private val irCacheStoreFieldFactory: IrCacheStoreFieldFactory,
    private val irCacheableExpressionBodyFactory: IrCacheableExpressionBodyFactory,
) : IrElementTransformerVoid() {
    override fun visitFunction(declaration: IrFunction): IrStatement {
        if (!declaration.hasAnnotation(cacheableAnnotation)) {
            return super.visitFunction(declaration)
        }

        val parent = (declaration.parent as? IrDeclarationContainer)
            ?: error("Unexpected function parent type")

        val irCacheStoreField = irCacheStoreFieldFactory.create(declaration)
        parent.declarations.add(irCacheStoreField)

        declaration.body = irCacheableExpressionBodyFactory.create(declaration)

        return declaration
    }

    companion object {
        val cacheableAnnotation = FqName("com.moriatsushi.cacheable.Cacheable")
    }
}
