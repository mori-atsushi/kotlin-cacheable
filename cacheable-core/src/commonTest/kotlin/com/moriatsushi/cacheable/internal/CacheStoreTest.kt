package com.moriatsushi.cacheable.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class CacheStoreTest {
    @Test
    fun testCacheOrInvoke() {
        val cacheStore = CacheStore(FakeTimeProvider())
        val result1 = cacheStore.cacheOrInvoke { "value1" }
        assertEquals("value1", result1)

        val result2 = cacheStore.cacheOrInvoke { "value2" }
        assertEquals("value1", result2)
    }

    @Test
    fun testCacheOrInvoke_withKey() {
        val cacheStore = CacheStore(FakeTimeProvider())
        val result1 = cacheStore.cacheOrInvoke("key1") { "value1" }
        assertEquals("value1", result1)

        val result2 = cacheStore.cacheOrInvoke("key2") { "value2" }
        assertEquals("value2", result2)

        val result3 = cacheStore.cacheOrInvoke("key1") { "value3" }
        assertEquals("value1", result3)

        val result4 = cacheStore.cacheOrInvoke("key2") { "value4" }
        assertEquals("value2", result4)
    }

    @Test
    fun testCacheOrInvoke_cacheNull() {
        val cacheStore = CacheStore(FakeTimeProvider())
        val result1 = cacheStore.cacheOrInvoke<String?> { null }
        assertEquals(null, result1)

        val result2 = cacheStore.cacheOrInvoke<String?> { "value" }
        assertEquals(null, result2)
    }
}
