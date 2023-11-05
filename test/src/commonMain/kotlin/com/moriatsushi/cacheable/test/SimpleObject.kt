package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable

object SimpleObject {
    var count = 0

    @Cacheable
    fun cacheableInt(): Int {
        count++
        return count
    }

    @Cacheable
    fun cacheableString(): String {
        count++
        return count.toString()
    }
}
