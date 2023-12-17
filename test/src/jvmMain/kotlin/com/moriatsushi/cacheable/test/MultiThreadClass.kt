package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable

class MultiThreadClass {
    private var count = 0

    @Cacheable(lock = true)
    fun withLock(): Int {
        Thread.sleep(100)
        count++
        return count
    }

    @Cacheable
    fun withoutLock(): Int {
        Thread.sleep(30)
        count++
        return count
    }
}
