package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleObjectTest {
    @Test
    fun testCacheable() {
        val int1 = SimpleObject.cacheableInt()
        assertEquals(1, int1)
        val int2 = SimpleObject.cacheableInt()
        assertEquals(int1, int2)

        val string1 = SimpleObject.cacheableString()
        assertEquals("2", string1)
        val string2 = SimpleObject.cacheableString()
        assertEquals(string1, string2)
    }
}
