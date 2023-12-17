package com.moriatsushi.cacheable.internal

import co.touchlab.stately.collections.ConcurrentMutableMap
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.coroutines.sync.Mutex

internal class LockStore<T>(private val lockFactory: () -> T) {
    private val lockMap = ConcurrentMutableMap<Any, T>()

    fun getLockKey(key: Any): T =
        lockMap.computeIfAbsent(key) { lockFactory() }

    fun removeLockKey(key: Any) {
        lockMap.remove(key)
    }

    companion object {
        val SynchronizedObjectStore: LockStore<SynchronizedObject> = LockStore(::SynchronizedObject)
        val MutexStore: LockStore<Mutex> = LockStore(::Mutex)
    }
}
