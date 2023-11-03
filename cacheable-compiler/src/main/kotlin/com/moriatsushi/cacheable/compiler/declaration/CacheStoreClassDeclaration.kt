package com.moriatsushi.cacheable.compiler.declaration

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrConstructorSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.createType
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class CacheStoreClassDeclaration(
    val irClassSymbol: IrClassSymbol,
) {
    val irType: IrSimpleType
        get() = irClassSymbol.createType(false, emptyList())

    val irConstructorSymbol: IrConstructorSymbol
        get() = irClassSymbol.constructors.single()

    val irCacheOrInvokeFunctionSymbol: IrSimpleFunctionSymbol
        get() = irClassSymbol.functions
            .single { it.owner.name == cacheOrInvokedFunctionName }

    companion object {
        private val internalPackageName = FqName("com.moriatsushi.cacheable.internal")
        private val cacheStoreId = ClassId(internalPackageName, Name.identifier("CacheStore"))
        private val cacheOrInvokedFunctionName = Name.identifier("cacheOrInvoke")

        fun find(pluginContext: IrPluginContext): CacheStoreClassDeclaration? =
            pluginContext.referenceClass(cacheStoreId)
                ?.let(::CacheStoreClassDeclaration)
    }
}
