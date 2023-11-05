package com.moriatsushi.cacheable.internal

/**
 * A cache store.
 */
internal class CacheStore {
    private var cacheMap = mutableMapOf<List<*>, Any?>()

    inline fun <T> cacheOrInvoke(vararg key: Any?, value: () -> T): T {
        val keyList = key.toList()
        val result = if (cacheMap.containsKey(keyList)) {
            cacheMap[keyList]
        } else {
            value().also { cacheMap[keyList] = it }
        }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }
}
