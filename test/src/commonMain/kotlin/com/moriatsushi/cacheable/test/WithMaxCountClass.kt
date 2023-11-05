package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable

class WithMaxCountClass {
    private var count = 0

    @Cacheable(maxCount = 2)
    fun cacheableInt(key: String): Int {
        count++
        return count
    }
}
