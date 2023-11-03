package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.name.Name

class IrCacheableFunctionFactory(
    private val irBuiltIns: IrBuiltIns,
    private val irFactory: IrFactory,
    private val cacheableDeclarations: CacheableDeclarations,
) {
    fun create(
        originalFunction: IrSimpleFunction,
        cacheStoreField: IrField,
    ): IrFunction {
        val outputFunction = originalFunction.copy()
        outputFunction.body = createBody(
            originalFunction = originalFunction,
            outputFunction = outputFunction,
            cacheStoreField = cacheStoreField,
        )
        return outputFunction
    }

    private fun createBody(
        originalFunction: IrSimpleFunction,
        outputFunction: IrSimpleFunction,
        cacheStoreField: IrField,
    ): IrBody {
        val lambda = irFactory.buildFun {
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            name = Name.special("<anonymous>")
            returnType = originalFunction.returnType
            visibility = DescriptorVisibilities.LOCAL
        }
        val originalCall = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = cacheStoreField.type,
            symbol = originalFunction.symbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 0,
        )
        lambda.parent = outputFunction
        lambda.body = irFactory.createExpressionBody(originalCall)
        val lambdaExpression = IrFunctionExpressionImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = irBuiltIns.functionN(0).typeWith(listOf(originalFunction.returnType)),
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
        return irFactory.createExpressionBody(expressionCall)
    }

    private fun IrSimpleFunction.copy(): IrSimpleFunction {
        val copied = irFactory.createSimpleFunction(
            startOffset = startOffset,
            endOffset = endOffset,
            origin = origin,
            name = name,
            visibility = visibility,
            isInline = isInline,
            isExpect = isExpect,
            returnType = returnType,
            modality = modality,
            symbol = IrSimpleFunctionSymbolImpl(),
            isTailrec = isTailrec,
            isSuspend = isSuspend,
            isOperator = isOperator,
            isInfix = isInfix,
            isExternal = isExternal,
            containerSource = containerSource,
            isFakeOverride = isFakeOverride,
        )
        copied.parent = parent
        return copied
    }
}
