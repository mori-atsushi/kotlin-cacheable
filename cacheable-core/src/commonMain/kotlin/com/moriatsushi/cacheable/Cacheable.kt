package com.moriatsushi.cacheable

/**
 * An annotation for caching the result of a function.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Cacheable
