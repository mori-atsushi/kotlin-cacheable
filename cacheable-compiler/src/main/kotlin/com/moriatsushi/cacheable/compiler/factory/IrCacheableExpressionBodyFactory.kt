package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl

class IrCacheableExpressionBodyFactory(
    private val irFactory: IrFactory,
    private val cacheableDeclarations: CacheableDeclarations,
) {
    fun create(
        originalFunction: IrFunction,
        cacheStoreField: IrField,
    ): IrExpressionBody {
        val simpleCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = originalFunction.returnType,
            symbol = cacheableDeclarations.cacheStoreClassDeclaration
                .irCacheOrInvokeFunctionSymbol,
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
