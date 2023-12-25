package com.moriatsushi.cacheable.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class LockStoreTest {
    @Test
    fun testGetLockKey_sameKeys() {
        val lockStore = LockStore.createSynchronizedObjectStore()

        val result1 = lockStore.getLockKey("key1")
        val result2 = lockStore.getLockKey("key1")
        assertEquals(result1, result2)
    }

    @Test
    fun testTetLockKey_differentKeys() {
        val lockStore = LockStore.createSynchronizedObjectStore()

        val result1 = lockStore.getLockKey("key1")
        val result2 = lockStore.getLockKey("key2")
        assertNotEquals(result1, result2)
    }

    @Test
    fun testRemoveLockKey() {
        val lockStore = LockStore.createSynchronizedObjectStore()

        val result1 = lockStore.getLockKey("key1")
        val result2 = lockStore.getLockKey("key1")
        lockStore.removeLockKey("key1")
        lockStore.removeLockKey("key1")

        val result3 = lockStore.getLockKey("key1")
        assertEquals(result1, result2)
        assertNotEquals(result1, result3)
    }
}
