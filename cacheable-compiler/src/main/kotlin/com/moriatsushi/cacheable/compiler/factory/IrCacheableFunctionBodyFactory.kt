package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.createBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrReturnImpl
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.name.Name

class IrCacheableFunctionBodyFactory(
    private val irBuiltIns: IrBuiltIns,
    private val irFactory: IrFactory,
    private val cacheableDeclarations: CacheableDeclarations,
) {
    fun create(
        originalFunction: IrSimpleFunction,
        actualFunction: IrSimpleFunction,
        cacheStoreField: IrField,
    ): IrBody {
        val lambda = irFactory.buildFun {
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            name = Name.special("<anonymous>")
            returnType = actualFunction.returnType
            visibility = DescriptorVisibilities.LOCAL
        }
        val originalCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = cacheStoreField.type,
            symbol = actualFunction.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 0,
        )
        lambda.parent = originalFunction
        lambda.body = createSingleLineBlockBody(
            type = actualFunction.returnType,
            returnTargetSymbol = lambda.symbol,
            value = originalCall,
        )
        val lambdaExpression = IrFunctionExpressionImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = irBuiltIns.functionN(0).typeWith(listOf(actualFunction.returnType)),
            function = lambda,
            origin = IrStatementOrigin.LAMBDA,
        )
        val expressionCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = cacheStoreField.type,
            symbol = cacheableDeclarations.cacheStoreClassDeclaration
                .irCacheOrInvokeFunctionSymbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 1,
        )
        expressionCall.putValueArgument(0, lambdaExpression)
        expressionCall.dispatchReceiver = IrGetFieldImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            symbol = cacheStoreField.symbol,
            type = cacheStoreField.type,
        )
        return createSingleLineBlockBody(
            type = originalFunction.returnType,
            returnTargetSymbol = originalFunction.symbol,
            value = expressionCall,
        )
    }

    private fun createSingleLineBlockBody(
        type: IrType,
        returnTargetSymbol: IrReturnTargetSymbol,
        value: IrExpression,
    ): IrBlockBody = irFactory.createBlockBody(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        listOf(
            IrReturnImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                type = type,
                returnTargetSymbol = returnTargetSymbol,
                value = value,
            ),
        ),
    )
}
