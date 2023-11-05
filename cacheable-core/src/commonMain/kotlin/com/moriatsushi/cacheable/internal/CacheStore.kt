package com.moriatsushi.cacheable.internal

import com.moriatsushi.cacheable.CacheableConfiguration
import com.moriatsushi.cacheable.UNLIMITED_CACHE_COUNT

/**
 * A cache store.
 */
internal class CacheStore(
    private val maxCount: Int,
    private val currentEpochMillis: () -> Long,
) {
    constructor(maxCount: Int = UNLIMITED_CACHE_COUNT) : this(
        maxCount,
        { CacheableConfiguration.timeProvider.currentEpochMillis },
    )

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
        if (maxCount != UNLIMITED_CACHE_COUNT && cacheMap.size >= maxCount) {
            val oldestEntry = cacheMap.minByOrNull { it.value.lastAccessedEpochMillis }
            if (oldestEntry != null) {
                cacheMap.remove(oldestEntry.key)
            }
        }

        cacheMap[key] = CacheEntry(value, currentEpochMillis())
    }
}
