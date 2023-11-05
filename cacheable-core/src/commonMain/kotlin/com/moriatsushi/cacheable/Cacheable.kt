package com.moriatsushi.cacheable

/**
 * An annotation for caching the result of a function. When you call a function annotated with this,
 * the function will not be invoked and the cached value will be returned if the function has been
 * called before with the same arguments. When a cacheable function is in a class, the cache is only
 * shared within the instance of the class.
 *
 * @param maxCount The maximum number of caches. If the number of caches exceeds this value, the
 * last used cache will be deleted. The default value is [unlimited][UNLIMITED_CACHE_COUNT].
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Cacheable(val maxCount: Int = UNLIMITED_CACHE_COUNT)

/**
 * A constant indicating that the number of caches is unlimited.
 */
const val UNLIMITED_CACHE_COUNT = -1
