package com.moriatsushi.cacheable.compiler.factory

import org.jetbrains.kotlin.backend.common.ir.moveBodyTo
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.symbols.impl.IrSimpleFunctionSymbolImpl
import org.jetbrains.kotlin.name.Name

class IrActualFunctionFactory(
    private val irFactory: IrFactory,
) {
    fun create(originalFunction: IrSimpleFunction): IrSimpleFunction {
        val actualFunction = originalFunction.copy()
        actualFunction.name = Name.identifier("_${originalFunction.name}_actual")
        actualFunction.body = originalFunction.moveBodyTo(actualFunction)
        return actualFunction
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
