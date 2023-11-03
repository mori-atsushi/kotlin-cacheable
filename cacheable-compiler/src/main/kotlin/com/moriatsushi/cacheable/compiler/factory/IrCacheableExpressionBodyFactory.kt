package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.resolver.ClassResolver
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.name.Name

class IrCacheableExpressionBodyFactory(
    private val irFactory: IrFactory,
    private val classResolver: ClassResolver,
) {
    fun create(
        originalFunction: IrFunction,
        cacheStoreField: IrField,
    ): IrExpressionBody {
        val simpleCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = originalFunction.returnType,
            symbol = classResolver.irCacheStoreClass.functions
                .single { it.owner.name == Name.identifier("cacheOrInvoke") },
            typeArgumentsCount = 0,
            valueArgumentsCount = 0
        )
        simpleCall.dispatchReceiver = IrGetFieldImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            symbol = cacheStoreField.symbol,
            type = cacheStoreField.type
        )
        return irFactory.createExpressionBody(simpleCall)
    }
}
