package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals

class TopLevelFunctionTest {
    @Test
    fun test() {
        val int1 = cacheableInt()
        assertEquals(1, int1)
        val int2 = cacheableInt()
        assertEquals(int1, int2)

        val string1 = cacheableString()
        assertEquals("2", string1)
        val string2 = cacheableString()
        assertEquals(string1, string2)
    }
}
