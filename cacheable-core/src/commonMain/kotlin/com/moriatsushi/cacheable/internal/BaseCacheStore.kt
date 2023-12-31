package com.moriatsushi.cacheable.internal

import co.touchlab.stately.collections.ConcurrentMutableMap
import com.moriatsushi.cacheable.CacheableConfiguration
import com.moriatsushi.cacheable.UNLIMITED_CACHE_COUNT

/**
 * A base cache store.
 */
internal class BaseCacheStore(
    private val maxCount: Int,
    private val currentEpochMillis: () -> Long,
) {
    private val cacheMap = ConcurrentMutableMap<Any, CacheEntry>()

    constructor(maxCount: Int = UNLIMITED_CACHE_COUNT) : this(
        maxCount,
        { CacheableConfiguration.timeProvider.currentEpochMillis },
    )

    inline fun <T> cacheOrInvoke(key: Any, value: () -> T): T {
        val cacheEntry = getCacheEntry(key)
        if (cacheEntry != null) {
            @Suppress("UNCHECKED_CAST")
            return cacheEntry.value as T
        }

        val newValue = value()
        saveCacheEntry(key, newValue)
        return newValue
    }

    private fun getCacheEntry(key: Any): CacheEntry? =
        cacheMap.block { map ->
            val entry = map[key]
            if (entry != null) {
                entry.lastAccessedEpochMillis = currentEpochMillis()
            }
            entry
        }

    private fun saveCacheEntry(key: Any, value: Any?) {
        cacheMap.block { map ->
            if (maxCount != UNLIMITED_CACHE_COUNT && map.size >= maxCount) {
                val oldestEntry = map.minByOrNull { it.value.lastAccessedEpochMillis }
                if (oldestEntry != null) {
                    map.remove(oldestEntry.key)
                }
            }

            map[key] = CacheEntry(value, currentEpochMillis())
        }
    }
}
