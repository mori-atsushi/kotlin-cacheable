package com.moriatsushi.cacheable

/**
 * An annotation for caching the result of a function. When you call a function annotated with this,
 * the function will not be invoked and the cached value will be returned if the function has been
 * called before with the same arguments. When a cacheable function is in a class, the cache is only
 * shared within the instance of the class.
 *
 * @param maxCount The maximum number of caches. If the number of caches exceeds this value, the
 * last used cache will be deleted. The default value is [unlimited][UNLIMITED_CACHE_COUNT].
 * @param lock When this is true, the function is guaranteed to be called only once even if the
 * function is called multiple times with the same arguments at the same time. Otherwise, the
 * function may be called multiple times with the same arguments at the same time. The default value
 * is false.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Cacheable(
    val maxCount: Int = UNLIMITED_CACHE_COUNT,
    val lock: Boolean = false,
)

/**
 * A constant indicating that the number of caches is unlimited.
 */
const val UNLIMITED_CACHE_COUNT = -1
