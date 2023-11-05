package com.moriatsushi.cacheable.compiler.declaration

import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.declarations.IrAnnotationContainer
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.FqName

class CacheableAnnotationDeclaration {
    fun hasAnnotation(annotationContainer: IrAnnotationContainer): Boolean =
        annotationContainer.hasAnnotation(name)

    fun findMaxCountExpression(annotationContainer: IrAnnotationContainer): IrExpression? =
        annotationContainer.getAnnotation(name)
            ?.valueArguments
            ?.firstOrNull()

    companion object {
        val name = FqName("com.moriatsushi.cacheable.Cacheable")
    }
}
