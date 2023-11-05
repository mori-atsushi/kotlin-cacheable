package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable

class ClassWithCompanionObject {
    @Cacheable
    fun cacheableInt(): Int {
        count++
        return count
    }

    companion object {
        private var count = 0

        @Cacheable
        fun cacheableInt(): Int {
            count++
            return count
        }
    }
}
