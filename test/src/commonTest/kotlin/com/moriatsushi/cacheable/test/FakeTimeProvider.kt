package com.moriatsushi.cacheable.test

import com.moriatsushi.cacheable.CacheableTimeProvider

class FakeTimeProvider : CacheableTimeProvider {
    override var currentEpochMillis: Long = 0
}
