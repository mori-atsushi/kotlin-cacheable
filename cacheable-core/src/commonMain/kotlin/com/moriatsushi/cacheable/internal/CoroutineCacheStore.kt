package com.moriatsushi.cacheable.internal

import com.moriatsushi.cacheable.UNLIMITED_CACHE_COUNT
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A cache store for a suspend function.
 */
internal class CoroutineCacheStore(
    private val cacheStore: BaseCacheStore = BaseCacheStore(),
    private val lockStore: LockStore<Mutex> = LockStore.MutexStore,
    private val lock: Boolean = false,
) {
    constructor(maxCount: Int = UNLIMITED_CACHE_COUNT) : this(BaseCacheStore(maxCount))

    @Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
    suspend inline fun <T> cacheOrInvoke(vararg key: Any?, value: suspend () -> T): T {
        val keyList = key.toList()
        return if (lock) {
            withLock(keyList) {
                cacheStore.cacheOrInvoke(keyList) { value() }
            }
        } else {
            cacheStore.cacheOrInvoke(keyList) { value() }
        }
    }

    private suspend inline fun <T> withLock(key: Any, block: () -> T): T {
        val mutex = lockStore.getLockKey(key)
        return mutex.withLock {
            try {
                block()
            } finally {
                lockStore.removeLockKey(key)
            }
        }
    }
}
