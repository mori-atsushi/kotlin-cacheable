package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.Cacheable

var globalCount = 0

@Cacheable
fun cacheableInt(): Int {
    globalCount++
    return globalCount
}

@Cacheable
fun cacheableString(): String {
    globalCount++
    return globalCount.toString()
}

@Cacheable
fun cacheableIntWithKey(key: String): Int {
    globalCount++
    return globalCount
}
