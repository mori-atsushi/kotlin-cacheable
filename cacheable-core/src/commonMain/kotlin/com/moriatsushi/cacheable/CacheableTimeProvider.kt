package com.moriatsushi.cacheable

/**
 * An interface that provides the current epoch milliseconds.
 */
interface CacheableTimeProvider {
    val currentEpochMillis: Long
}
