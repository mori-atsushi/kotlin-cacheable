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
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
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
            valueParameters = originalFunction.valueParameters,
            thisReceiver = thisReceiver,
        )
        val lambdaExpression = createLambdaExpression(
            actualCall = actualCall,
            parent = originalFunction,
        )
        val cacheStoreCall = createCacheStoreCall(
            cacheStoreField = cacheStoreField,
            valueParameters = originalFunction.valueParameters,
            lambdaExpression = lambdaExpression,
            thisReceiver = thisReceiver,
            typeArgument = originalFunction.returnType,
        )
        return createSingleLineBlockBody(
            type = originalFunction.returnType,
            returnTargetSymbol = originalFunction.symbol,
            value = cacheStoreCall,
        )
    }

    private fun createActualCall(
        actualFunction: IrSimpleFunction,
        valueParameters: List<IrValueParameter>,
        thisReceiver: IrExpression?,
    ): IrCall {
        assert(valueParameters.size == actualFunction.valueParameters.size)
        val actualCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = actualFunction.returnType,
            symbol = actualFunction.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = actualFunction.valueParameters.size,
        )
        actualCall.dispatchReceiver = thisReceiver
        for (valueParameter in valueParameters) {
            val argument = IrGetValueImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                symbol = valueParameter.symbol,
                type = valueParameter.type,
            )
            actualCall.putValueArgument(valueParameter.index, argument)
        }
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
        valueParameters: List<IrValueParameter>,
        lambdaExpression: IrExpression,
        thisReceiver: IrExpression?,
        typeArgument: IrType,
    ): IrCall {
        val dispatchReceiver = IrGetFieldImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            symbol = cacheStoreField.symbol,
            type = cacheStoreField.type,
        )
        dispatchReceiver.receiver = thisReceiver
        val keyElements = valueParameters.map {
            IrGetValueImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                symbol = it.symbol,
                type = it.type,
            )
        }
        return cacheableDeclarations.cacheStoreClassDeclaration
            .createCacheOrInvokeFunctionCall(
                typeArgument = typeArgument,
                keyElements = keyElements,
                blockExpression = lambdaExpression,
                dispatchReceiver = dispatchReceiver,
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
