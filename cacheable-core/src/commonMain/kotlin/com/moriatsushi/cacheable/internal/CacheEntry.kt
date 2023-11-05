package com.moriatsushi.cacheable.internal

internal data class CacheEntry(
    val value: Any?,
    var lastAccessedEpochMillis: Long,
)
