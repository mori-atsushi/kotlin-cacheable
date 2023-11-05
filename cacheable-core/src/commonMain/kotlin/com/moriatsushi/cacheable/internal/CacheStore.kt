package com.moriatsushi.cacheable.internal

import kotlinx.datetime.Clock

/**
 * A cache store.
 */
internal class CacheStore {
    private var cacheMap = mutableMapOf<Any, CacheEntry>()

    inline fun <T> cacheOrInvoke(vararg key: Any?, value: () -> T): T {
        val keyList = key.toList()
        val cacheEntry = getCacheEntry(keyList)
        if (cacheEntry != null) {
            @Suppress("UNCHECKED_CAST")
            return cacheEntry.value as T
        }

        val newValue = value()
        saveCacheEntry(keyList, newValue)
        return newValue
    }

    private fun getCacheEntry(key: Any): CacheEntry? {
        val entry = cacheMap[key]
        entry?.lastAccessedEpochMillis = currentEpochMillis()
        return entry
    }

    private fun saveCacheEntry(key: Any, value: Any?) {
        cacheMap[key] = CacheEntry(value, currentEpochMillis())
    }

    private fun currentEpochMillis(): Long =
        Clock.System.now().toEpochMilliseconds()
}
