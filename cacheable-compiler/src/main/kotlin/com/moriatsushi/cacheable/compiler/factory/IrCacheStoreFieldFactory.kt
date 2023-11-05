package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.isTopLevel
import org.jetbrains.kotlin.name.Name

class IrCacheStoreFieldFactory(
    private val irFactory: IrFactory,
    private val cacheableDeclarations: CacheableDeclarations,
) {
    fun create(function: IrFunction): IrField {
        val field = irFactory.buildField {
            name = Name.identifier(function.name.identifier + CACHE_STORE_SUFFIX)
            type = cacheableDeclarations.cacheStoreClassDeclaration.irType
            visibility = DescriptorVisibilities.PRIVATE
            isStatic = function.isTopLevel
        }
        field.initializer = irFactory.createExpressionBody(
            IrConstructorCallImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                type = cacheableDeclarations.cacheStoreClassDeclaration.irType,
                symbol = cacheableDeclarations.cacheStoreClassDeclaration.irConstructorSymbol,
                typeArgumentsCount = 0,
                constructorTypeArgumentsCount = 0,
                valueArgumentsCount = 0,
                origin = IrStatementOrigin.EQ,
            ),
        )
        field.parent = function.parent
        return field
    }

    companion object {
        private const val CACHE_STORE_SUFFIX = "\$cacheStore"
    }
}
