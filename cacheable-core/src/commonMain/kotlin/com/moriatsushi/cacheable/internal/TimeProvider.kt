package com.moriatsushi.cacheable.internal

internal interface TimeProvider {
    val currentEpochMillis: Long
}
