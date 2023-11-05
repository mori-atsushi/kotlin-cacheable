package com.moriatsushi.cacheable.compiler.factory

import org.jetbrains.kotlin.backend.common.ir.moveBodyTo
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.copyParameterDeclarationsFrom
import org.jetbrains.kotlin.name.Name

class IrActualFunctionFactory(
    private val irFactory: IrFactory,
) {
    fun create(originalFunction: IrSimpleFunction): IrSimpleFunction =
        originalFunction.renamed(
            Name.identifier(originalFunction.name.identifier + ACTUAL_SUFFIX),
        )

    private fun IrSimpleFunction.renamed(name: Name): IrSimpleFunction {
        val original = this
        val copied = irFactory.buildFun {
            this.updateFrom(original)
            this.name = name
            this.returnType = original.returnType
        }
        copied.parent = original.parent
        copied.copyParameterDeclarationsFrom(original)
        copied.body = original.moveBodyTo(copied)
        return copied
    }

    companion object {
        private const val ACTUAL_SUFFIX = "\$actual"
    }
}
