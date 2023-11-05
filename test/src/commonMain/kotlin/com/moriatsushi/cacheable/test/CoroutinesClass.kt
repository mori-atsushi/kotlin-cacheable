package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable
import kotlinx.coroutines.delay

class CoroutinesClass {
    private var count = 0

    @Cacheable
    suspend fun cacheableInt(): Int {
        delay(100)
        count++
        return count
    }

    @Cacheable
    suspend fun cacheableWithKey(key: String): Int {
        delay(100)
        count++
        return count
    }
}
