package com.moriatsushi.cacheable.internal

import com.moriatsushi.cacheable.CacheableConfiguration
import com.moriatsushi.cacheable.UNLIMITED_CACHE_COUNT
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

/**
 * A cache store.
 */
internal class CacheStore(
    private val maxCount: Int,
    private val currentEpochMillis: () -> Long,
) {
    private val lock = SynchronizedObject()
    private var cacheMap = mutableMapOf<Any, CacheEntry>()

    constructor(maxCount: Int = UNLIMITED_CACHE_COUNT) : this(
        maxCount,
        { CacheableConfiguration.timeProvider.currentEpochMillis },
    )

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

    private fun getCacheEntry(key: Any): CacheEntry? = synchronized(lock) {
        cacheMap[key].also {
            it?.lastAccessedEpochMillis = currentEpochMillis()
        }
    }

    private fun saveCacheEntry(key: Any, value: Any?) {
        synchronized(lock) {
            if (maxCount != UNLIMITED_CACHE_COUNT && cacheMap.size >= maxCount) {
                val oldestEntry = cacheMap.minByOrNull { it.value.lastAccessedEpochMillis }
                if (oldestEntry != null) {
                    cacheMap.remove(oldestEntry.key)
                }
            }

            cacheMap[key] = CacheEntry(value, currentEpochMillis())
        }
    }
}
