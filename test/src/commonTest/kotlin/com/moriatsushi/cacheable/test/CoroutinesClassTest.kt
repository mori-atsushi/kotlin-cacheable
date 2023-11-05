package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class CoroutinesClassTest {
    @Test
    fun test() = runTest {
        val target = CoroutinesClass()
        val int1 = target.cacheableInt()
        assertEquals(1, int1)
        val int2 = target.cacheableInt()
        assertEquals(int1, int2)
    }

    @Test
    fun testWithKey() = runTest {
        val target = CoroutinesClass()
        val int1 = target.cacheableWithKey("key1")
        assertEquals(1, int1)
        val int2 = target.cacheableWithKey("key1")
        assertEquals(int1, int2)

        val int3 = target.cacheableWithKey("key2")
        assertEquals(2, int3)
        val int4 = target.cacheableWithKey("key2")
        assertEquals(int3, int4)
    }
}
