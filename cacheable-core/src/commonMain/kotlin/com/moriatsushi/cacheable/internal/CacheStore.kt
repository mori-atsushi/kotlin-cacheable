package com.moriatsushi.cacheable.internal

/**
 * A cache store.
 */
internal class CacheStore {
    private var cache: Any? = null

    @Suppress("UNCHECKED_CAST")
    inline fun <T> cacheOrInvoke(value: () -> T): T =
        (cache ?: value().also { cache = it }) as T
}
