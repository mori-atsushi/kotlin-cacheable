package com.moriatsushi.cacheable.compiler.declaration

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstructorCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrVarargElement
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrVarargImpl
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.createType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class CacheStoreClassDeclaration(
    private val irClassSymbol: IrClassSymbol,
    private val irBuiltIns: IrBuiltIns,
) {
    val irType: IrSimpleType
        get() = irClassSymbol.createType(false, emptyList())

    private val irConstructorSymbol: IrConstructorSymbol
        get() = irClassSymbol.constructors.single()

    private val irCacheOrInvokeFunctionSymbol: IrSimpleFunctionSymbol
        get() = irClassSymbol.functions
            .single { it.owner.name == cacheOrInvokedFunctionName }

    val constructorCall: IrConstructorCall
        get() = IrConstructorCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = irType,
            symbol = irConstructorSymbol,
            typeArgumentsCount = 0,
            constructorTypeArgumentsCount = 0,
            valueArgumentsCount = 0,
        )

    fun createCacheOrInvokeFunctionCall(
        typeArgument: IrType,
        keyElements: List<IrVarargElement>,
        blockExpression: IrExpression,
        dispatchReceiver: IrExpression? = null,
    ): IrCall {
        val call = IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = typeArgument,
            symbol = irCacheOrInvokeFunctionSymbol,
            typeArgumentsCount = 1,
            valueArgumentsCount = 2,
        )
        call.dispatchReceiver = dispatchReceiver
        val keyArgument = IrVarargImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = irBuiltIns.arrayClass.typeWith(irBuiltIns.anyNType),
            varargElementType = irBuiltIns.anyNType,
            elements = keyElements,
        )
        call.putValueArgument(0, keyArgument)
        call.putValueArgument(1, blockExpression)
        call.putTypeArgument(0, typeArgument)
        return call
    }

    companion object {
        private val internalPackageName = FqName("com.moriatsushi.cacheable.internal")
        private val cacheStoreId = ClassId(internalPackageName, Name.identifier("CacheStore"))
        private val cacheOrInvokedFunctionName = Name.identifier("cacheOrInvoke")

        fun find(pluginContext: IrPluginContext): CacheStoreClassDeclaration? =
            pluginContext.referenceClass(cacheStoreId)?.let {
                CacheStoreClassDeclaration(it, pluginContext.irBuiltIns)
            }
    }
}
