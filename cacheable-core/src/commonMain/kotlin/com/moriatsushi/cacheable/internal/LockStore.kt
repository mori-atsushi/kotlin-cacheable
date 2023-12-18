package com.moriatsushi.cacheable.internal

import co.touchlab.stately.collections.ConcurrentMutableMap
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.coroutines.sync.Mutex

internal class LockStore<T>(private val lockFactory: () -> T) {
    private val lockMap = ConcurrentMutableMap<Any, Entry<T>>()

    fun getLockKey(key: Any): T =
        lockMap.block {
            val entry = lockMap.getOrPut(key) { Entry(lockFactory()) }
            entry.referrerCount++
            entry.lock
        }

    fun removeLockKey(key: Any) {
        lockMap.block {
            val entry = lockMap[key] ?: return@block
            entry.referrerCount--
            if (entry.referrerCount <= 0) {
                lockMap.remove(key)
            }
        }
    }

    private data class Entry<T>(val lock: T, var referrerCount: Int = 0)

    companion object {
        fun createSynchronizedObjectStore(): LockStore<SynchronizedObject> =
            LockStore(::SynchronizedObject)

        fun createMutexStore(): LockStore<Mutex> = LockStore(::Mutex)
    }
}
