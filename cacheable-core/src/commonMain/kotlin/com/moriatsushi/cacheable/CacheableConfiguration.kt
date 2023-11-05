package com.moriatsushi.cacheable

import com.moriatsushi.cacheable.internal.DefaultTimeProvider

/**
 * An object that holds the configuration of the cacheable library.
 */
object CacheableConfiguration {
    /**
     * A current time provider that is used to record the last access time of the cache.
     */
    var timeProvider: CacheableTimeProvider = DefaultTimeProvider

    /**
     * Resets the [timeProvider] to the default.
     */
    fun resetTimeProvider() {
        timeProvider = DefaultTimeProvider
    }
}
