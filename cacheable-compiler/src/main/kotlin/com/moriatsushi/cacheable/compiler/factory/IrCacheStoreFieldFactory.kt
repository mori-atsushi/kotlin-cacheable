package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.resolver.ClassResolver
import com.moriatsushi.cacheable.compiler.resolver.TypeResolver
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.name.Name

class IrCacheStoreFieldFactory(
    private val irFactory: IrFactory,
    private val classResolver: ClassResolver,
    private val typeResolver: TypeResolver,
) {
    fun create(function: IrFunction): IrField {
        val field = irFactory.buildField {
            name = Name.identifier("_${function.name.identifier}_cache")
            type = typeResolver.irCacheStoreType
            visibility = DescriptorVisibilities.PRIVATE
            isStatic = true
        }
        field.initializer = irFactory.createExpressionBody(
            IrConstructorCallImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                type = typeResolver.irCacheStoreType,
                symbol = classResolver.irCacheStoreClass.constructors.single(),
                typeArgumentsCount = 0,
                constructorTypeArgumentsCount = 0,
                valueArgumentsCount = 0,
                origin = IrStatementOrigin.EQ,
            ),
        )
        field.parent = function.parent
        return field
    }
}
