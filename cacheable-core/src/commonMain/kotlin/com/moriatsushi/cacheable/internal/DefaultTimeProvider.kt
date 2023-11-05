package com.moriatsushi.cacheable.internal

import com.moriatsushi.cacheable.CacheableTimeProvider
import kotlinx.datetime.Clock

internal object DefaultTimeProvider : CacheableTimeProvider {
    override val currentEpochMillis: Long
        get() = Clock.System.now().toEpochMilliseconds()
}
