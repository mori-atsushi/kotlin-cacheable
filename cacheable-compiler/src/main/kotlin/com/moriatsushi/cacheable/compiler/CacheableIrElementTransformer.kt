package com.moriatsushi.cacheable.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.isNullableAny
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class CacheableIrElementTransformer(
    private val pluginContext: IrPluginContext,
) : IrElementTransformerVoid() {
    private val printlnSymbol: IrSimpleFunctionSymbol = pluginContext
        .referenceFunctions(
            CallableId(
                packageName = FqName("kotlin.io"),
                callableName = Name.identifier("println"),
            ),
        )
        .single {
            it.owner.valueParameters.size == 1 &&
                it.owner.valueParameters.single().type.isNullableAny()
        }

    override fun visitFunction(declaration: IrFunction): IrStatement {
        if (!declaration.hasAnnotation(cacheableAnnotation)) {
            return super.visitFunction(declaration)
        }

        val body = declaration.body as? IrBlockBody
            ?: error("Unexpected function body type")

        // TODO: This is an example implementation. Implement the actual logic.
        val printlnCall = createPrintlnCall(irString(declaration.name.identifier))
        body.statements.add(0, printlnCall)

        return super.visitFunction(declaration)
    }

    private fun createPrintlnCall(value: IrExpression): IrCall =
        IrCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = pluginContext.irBuiltIns.unitType,
            symbol = printlnSymbol,
            typeArgumentsCount = 0,
            valueArgumentsCount = 1,
        ).apply {
            putValueArgument(0, value)
        }

    private fun irString(value: String): IrConst<String> = IrConstImpl.string(
        startOffset = UNDEFINED_OFFSET,
        endOffset = UNDEFINED_OFFSET,
        type = pluginContext.irBuiltIns.stringType,
        value = value,
    )

    companion object {
        val cacheableAnnotation = FqName("com.moriatsushi.cacheable.Cacheable")
    }
}
