package com.moriatsushi.cacheable.compiler.factory

import com.moriatsushi.cacheable.compiler.declaration.CacheableDeclarations
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.declarations.IrFactory
import org.jetbrains.kotlin.ir.declarations.IrField
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.util.isSuspend
import org.jetbrains.kotlin.ir.util.isTopLevel
import org.jetbrains.kotlin.name.Name

class IrCacheStoreFieldFactory(
    private val irFactory: IrFactory,
    private val cacheableDeclarations: CacheableDeclarations,
) {
    fun create(function: IrFunction): IrField {
        val field = irFactory.buildField {
            name = Name.identifier(function.name.identifier + CACHE_STORE_SUFFIX)
            type = if (function.isSuspend) {
                cacheableDeclarations.coroutineCacheStoreClassDeclaration.irType
            } else {
                cacheableDeclarations.cacheStoreClassDeclaration.irType
            }
            visibility = DescriptorVisibilities.PRIVATE
            isStatic = function.isTopLevel
        }
        val maxCountExpression = cacheableDeclarations.cacheableAnnotationDeclaration
            .findMaxCountExpression(function)
        val lockExpression = cacheableDeclarations.cacheableAnnotationDeclaration
            .findLockExpression(function)
        val constructorCall = if (function.isSuspend) {
            cacheableDeclarations.coroutineCacheStoreClassDeclaration
                .createConstructorCall(maxCountExpression, lockExpression)
        } else {
            cacheableDeclarations.cacheStoreClassDeclaration
                .createConstructorCall(maxCountExpression, lockExpression)
        }
        field.initializer = irFactory.createExpressionBody(constructorCall)
        field.parent = function.parent
        return field
    }

    companion object {
        private const val CACHE_STORE_SUFFIX = "\$cacheStore"
    }
}
