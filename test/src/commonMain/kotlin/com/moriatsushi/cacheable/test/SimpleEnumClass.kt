package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable

enum class SimpleEnumClass(initial: Int) {
    A(10),
    B(20),
    ;

    private var count: Int = initial

    @Cacheable
    fun cacheableInt(): Int {
        count++
        return count
    }
}
