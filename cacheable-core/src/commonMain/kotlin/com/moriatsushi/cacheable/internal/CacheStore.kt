package com.moriatsushi.cacheable.internal

import com.moriatsushi.cacheable.UNLIMITED_CACHE_COUNT

/**
 * A cache store for a non-suspend function.
 */
internal class CacheStore(private val cacheStore: BaseCacheStore) {
    constructor(maxCount: Int = UNLIMITED_CACHE_COUNT) : this(BaseCacheStore(maxCount))

    inline fun <T> cacheOrInvoke(vararg key: Any?, value: () -> T): T =
        cacheStore.cacheOrInvoke(*key, value = value)
}
