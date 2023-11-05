package com.moriatsushi.cacheable.compiler

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import com.moriatsushi.cacheable.compiler.factory.IrActualFunctionFactory
import com.moriatsushi.cacheable.compiler.factory.IrCacheStoreFieldFactory
import com.moriatsushi.cacheable.compiler.factory.IrCacheableFunctionBodyFactory
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclarationContainer
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid

class CacheableIrElementTransformer(
    private val cacheableDeclarations: CacheableDeclarations,
    private val irCacheStoreFieldFactory: IrCacheStoreFieldFactory,
    private val irActualFunctionFactory: IrActualFunctionFactory,
    private val irCacheableFunctionBodyFactory: IrCacheableFunctionBodyFactory,
) : IrElementTransformerVoid() {
    override fun visitSimpleFunction(declaration: IrSimpleFunction): IrStatement {
        if (!cacheableDeclarations.cacheableAnnotationDeclaration.hasAnnotation(declaration)) {
            return declaration
        }

        val parent = (declaration.parent as? IrDeclarationContainer)
            ?: error("Unexpected function parent type")

        val irCacheStoreField = irCacheStoreFieldFactory.create(declaration)
        parent.declarations.add(irCacheStoreField)

        val actualFunction = irActualFunctionFactory.create(declaration)
        parent.declarations.add(actualFunction)

        declaration.body = irCacheableFunctionBodyFactory.create(
            originalFunction = declaration,
            actualFunction = actualFunction,
            cacheStoreField = irCacheStoreField,
        )
        return declaration
    }
}
