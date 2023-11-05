package com.moriatsushi.cacheable.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class CacheStoreTest {
    @Test
    fun testCacheOrInvoke() {
        val cacheStore = CacheStore(timeProvider = FakeTimeProvider())
        val result1 = cacheStore.cacheOrInvoke { "value1" }
        assertEquals("value1", result1)

        val result2 = cacheStore.cacheOrInvoke { "value2" }
        assertEquals("value1", result2)
    }

    @Test
    fun testCacheOrInvoke_withKey() {
        val cacheStore = CacheStore(timeProvider = FakeTimeProvider())
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
        val cacheStore = CacheStore(timeProvider = FakeTimeProvider())
        val result1 = cacheStore.cacheOrInvoke<String?> { null }
        assertEquals(null, result1)

        val result2 = cacheStore.cacheOrInvoke<String?> { "value" }
        assertEquals(null, result2)
    }

    @Test
    fun testCacheOrInvoke_withMaxCount() {
        val fakeTimeProvider = FakeTimeProvider()
        val cacheStore = CacheStore(maxCount = 2, timeProvider = fakeTimeProvider)

        fakeTimeProvider.currentEpochMillis = 0
        cacheStore.cacheOrInvoke("key1") { "value1" }
        fakeTimeProvider.currentEpochMillis = 1
        cacheStore.cacheOrInvoke("key2") { "value2" }
        fakeTimeProvider.currentEpochMillis = 2
        cacheStore.cacheOrInvoke("key1") { "value3" }
        fakeTimeProvider.currentEpochMillis = 3
        cacheStore.cacheOrInvoke("key3") { "value4" }

        fakeTimeProvider.currentEpochMillis = 4
        val result1 = cacheStore.cacheOrInvoke("key1") { "value5" }
        assertEquals("value1", result1)

        fakeTimeProvider.currentEpochMillis = 5
        val result2 = cacheStore.cacheOrInvoke("key2") { "value6" }
        assertEquals("value6", result2)

        fakeTimeProvider.currentEpochMillis = 6
        val result3 = cacheStore.cacheOrInvoke("key3") { "value7" }
        assertEquals("value7", result3)
    }
}
