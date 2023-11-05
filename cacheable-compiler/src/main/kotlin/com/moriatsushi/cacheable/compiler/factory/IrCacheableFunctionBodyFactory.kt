package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.createBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrReturnImpl
import org.jetbrains.kotlin.ir.symbols.IrReturnTargetSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.name.SpecialNames

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
        val thisReceiver = originalFunction.dispatchReceiverParameter?.let {
            IrGetValueImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                symbol = it.symbol,
                type = it.type,
            )
        }
        val actualCall = createActualCall(
            actualFunction = actualFunction,
            thisReceiver = thisReceiver,
        )
        val lambdaExpression = createLambdaExpression(
            actualCall = actualCall,
            parent = originalFunction,
        )
        val cacheStoreCall = createCacheStoreCall(
            cacheStoreField = cacheStoreField,
            thisReceiver = thisReceiver,
            lambdaExpression = lambdaExpression,
        )
        return createSingleLineBlockBody(
            type = originalFunction.returnType,
            returnTargetSymbol = originalFunction.symbol,
            value = cacheStoreCall,
        )
    }

    private fun createActualCall(
        actualFunction: IrSimpleFunction,
        thisReceiver: IrExpression?,
    ): IrCall {
        val actualCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = actualFunction.returnType,
            symbol = actualFunction.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 0,
        )
        actualCall.dispatchReceiver = thisReceiver
        return actualCall
    }

    private fun createLambdaExpression(
        actualCall: IrCall,
        parent: IrDeclarationParent,
    ): IrFunctionExpression {
        val lambda = irFactory.buildFun {
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            name = SpecialNames.ANONYMOUS
            returnType = actualCall.type
            visibility = DescriptorVisibilities.LOCAL
        }
        lambda.parent = parent
        lambda.body = createSingleLineBlockBody(
            type = actualCall.type,
            returnTargetSymbol = lambda.symbol,
            value = actualCall,
        )
        return IrFunctionExpressionImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = irBuiltIns.functionN(0).typeWith(listOf(actualCall.type)),
            function = lambda,
            origin = IrStatementOrigin.LAMBDA,
        )
    }

    private fun createCacheStoreCall(
        cacheStoreField: IrField,
        thisReceiver: IrExpression?,
        lambdaExpression: IrExpression,
    ): IrCall {
        val expressionCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = cacheStoreField.type,
            symbol = cacheableDeclarations.cacheStoreClassDeclaration
                .irCacheOrInvokeFunctionSymbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 1,
        )
        val dispatchReceiver = IrGetFieldImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            symbol = cacheStoreField.symbol,
            type = cacheStoreField.type,
        )
        dispatchReceiver.receiver = thisReceiver
        expressionCall.dispatchReceiver = dispatchReceiver
        expressionCall.putValueArgument(0, lambdaExpression)
        return expressionCall
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
