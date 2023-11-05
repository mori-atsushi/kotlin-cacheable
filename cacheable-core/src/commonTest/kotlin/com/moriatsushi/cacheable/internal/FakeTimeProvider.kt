package com.moriatsushi.cacheable.internal

internal class FakeTimeProvider : TimeProvider {
    override var currentEpochMillis: Long = 0
}
