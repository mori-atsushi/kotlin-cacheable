package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals

class TopLevelFunctionTest {
    @Test
    fun test() {
        globalCount = 0

        val int1 = cacheableInt()
        assertEquals(1, int1)
        val int2 = cacheableInt()
        assertEquals(int1, int2)

        val string1 = cacheableString()
        assertEquals("2", string1)
        val string2 = cacheableString()
        assertEquals(string1, string2)
    }

    @Test
    fun testWithKey() {
        globalCount = 0

        val intForKeyA1 = cacheableIntWithKey("key-a")
        assertEquals(1, intForKeyA1)
        val intForKeyB1 = cacheableIntWithKey("key-b")
        assertEquals(2, intForKeyB1)

        val intForKeyA2 = cacheableIntWithKey("key-a")
        assertEquals(1, intForKeyA2)
        val intForKeyB2 = cacheableIntWithKey("key-b")
        assertEquals(2, intForKeyB2)
    }
}
