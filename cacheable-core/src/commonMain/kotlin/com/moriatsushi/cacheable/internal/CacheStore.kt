package com.moriatsushi.cacheable.internal

/**
 * A cache store.
 *
 * **DO NOT USE THIS CLASS DIRECTLY.**
 * it is used by the compiler plugin.
 */
class CacheStore {
    private var cache: Any? = null

    fun cacheOrInvoke(): Int {
        return 100
    }
}
