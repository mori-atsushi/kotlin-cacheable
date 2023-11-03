package com.moriatsushi.cacheable.compiler.factory

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl

class IrCacheableExpressionBodyFactory(
    private val pluginContext: IrPluginContext,
) {
    private val irFactory: IrFactory = pluginContext.irFactory

    fun create(declaration: IrFunction): IrExpressionBody =
        irFactory.createExpressionBody(
            IrConstImpl.int(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                type = pluginContext.irBuiltIns.intType,
                value = 100,
            ),
        )
}
