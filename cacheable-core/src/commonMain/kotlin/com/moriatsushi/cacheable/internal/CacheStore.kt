package com.moriatsushi.cacheable.internal

import com.moriatsushi.cacheable.UNLIMITED_CACHE_COUNT
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

/**
 * A cache store for a non-suspend function.
 */
internal class CacheStore(
    private val cacheStore: BaseCacheStore = BaseCacheStore(),
    private val lockStore: LockStore<SynchronizedObject> = LockStore.SynchronizedObjectStore,
    private val lock: Boolean = false,
) {
    constructor(
        maxCount: Int = UNLIMITED_CACHE_COUNT,
        lock: Boolean = false,
    ) : this(BaseCacheStore(maxCount), lock = lock)

    inline fun <T> cacheOrInvoke(vararg key: Any?, value: () -> T): T {
        val keyList = key.toList()
        return if (lock) {
            withLock(keyList) {
                cacheStore.cacheOrInvoke(keyList, value = value)
            }
        } else {
            cacheStore.cacheOrInvoke(keyList, value = value)
        }
    }

    private inline fun <T> withLock(key: Any, block: () -> T): T {
        val lockKey = lockStore.getLockKey(key)
        return synchronized(lockKey) {
            try {
                block()
            } finally {
                lockStore.removeLockKey(key)
            }
        }
    }
}
