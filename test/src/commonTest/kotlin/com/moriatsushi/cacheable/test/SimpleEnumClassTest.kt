package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleEnumClassTest {
    @Test
    fun test() {
        val int1 = SimpleEnumClass.A.cacheableInt()
        assertEquals(11, int1)
        val int2 = SimpleEnumClass.A.cacheableInt()
        assertEquals(int1, int2)

        val int3 = SimpleEnumClass.B.cacheableInt()
        assertEquals(21, int3)
        val int4 = SimpleEnumClass.B.cacheableInt()
        assertEquals(int3, int4)
    }
}
