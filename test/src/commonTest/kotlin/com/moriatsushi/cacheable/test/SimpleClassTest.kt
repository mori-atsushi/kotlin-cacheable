package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleClassTest {
    @Test
    fun testCacheable() {
        val target = SimpleClass()
        val int1 = target.cacheableInt()
        assertEquals(1, int1)
        val int2 = target.cacheableInt()
        assertEquals(int1, int2)

        val string1 = target.cacheableString()
        assertEquals("2", string1)
        val string2 = target.cacheableString()
        assertEquals(string1, string2)
    }

    @Test
    fun testMultipleInstance() {
        val target1 = SimpleClass()
        target1.count = 2
        val int1 = target1.cacheableInt()
        assertEquals(3, int1)

        val target2 = SimpleClass()
        target2.count = 4
        val int2 = target2.cacheableInt()
        assertEquals(5, int2)
    }
}
