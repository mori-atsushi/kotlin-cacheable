package com.moriatsushi.cacheable.compiler.resolver

import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.createType

class TypeResolver(
    classResolver: ClassResolver,
) {
    val irCacheStoreType: IrSimpleType =
        classResolver.irCacheStoreClass.createType(false, emptyList())
}
