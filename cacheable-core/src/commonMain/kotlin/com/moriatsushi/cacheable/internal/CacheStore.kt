package com.moriatsushi.cacheable.internal

/**
 * A cache store.
 */
internal class CacheStore {
    private var cache: Any? = null

    fun cacheOrInvoke(): Int {
        return 100
    }
}
