package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.CacheableConfiguration
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class WithMaxCountClassTest {
    private val fakeTimeProvider = FakeTimeProvider()

    @BeforeTest
    fun setUp() {
        CacheableConfiguration.timeProvider = fakeTimeProvider
    }

    @AfterTest
    fun tearDown() {
        CacheableConfiguration.resetTimeProvider()
    }

    @Test
    fun test() {
        val target = WithMaxCountClass()

        fakeTimeProvider.currentEpochMillis = 0
        val result1 = target.cacheableInt("key1")
        assertEquals(1, result1)

        fakeTimeProvider.currentEpochMillis = 1
        val result2 = target.cacheableInt("key2")
        assertEquals(2, result2)

        fakeTimeProvider.currentEpochMillis = 2
        val result3 = target.cacheableInt("key1")
        assertEquals(1, result3)

        fakeTimeProvider.currentEpochMillis = 3
        val result4 = target.cacheableInt("key3")
        assertEquals(3, result4)

        fakeTimeProvider.currentEpochMillis = 4
        val result5 = target.cacheableInt("key1")
        assertEquals(1, result5)

        fakeTimeProvider.currentEpochMillis = 5
        val result6 = target.cacheableInt("key2")
        assertEquals(4, result6)

        fakeTimeProvider.currentEpochMillis = 6
        val result7 = target.cacheableInt("key3")
        assertEquals(5, result7)
    }
}
