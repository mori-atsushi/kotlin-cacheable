package com.moriatsushi.cacheable.test

import kotlin.test.Test
import kotlin.test.assertEquals

class ClassWithCompanionObjectTest {
    @Test
    fun test() {
        val target1 = ClassWithCompanionObject()
        val int1 = target1.cacheableInt()
        assertEquals(1, int1)
        val int2 = target1.cacheableInt()
        assertEquals(int1, int2)

        val target2 = ClassWithCompanionObject()
        val int3 = target2.cacheableInt()
        assertEquals(2, int3)
        val int4 = target2.cacheableInt()
        assertEquals(int3, int4)

        val int5 = ClassWithCompanionObject.cacheableInt()
        assertEquals(3, int5)
        val int6 = ClassWithCompanionObject.cacheableInt()
        assertEquals(int5, int6)
    }
}
