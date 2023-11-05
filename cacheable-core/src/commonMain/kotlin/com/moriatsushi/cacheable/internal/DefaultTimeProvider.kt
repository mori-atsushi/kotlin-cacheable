package com.moriatsushi.cacheable.internal

import kotlinx.datetime.Clock

internal object DefaultTimeProvider : TimeProvider {
    override val currentEpochMillis: Long
        get() = Clock.System.now().toEpochMilliseconds()
}
