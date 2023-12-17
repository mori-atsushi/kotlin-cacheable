package com.moriatsushi.cacheable.internal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

class CoroutineCacheStoreTest {
    @Test
    fun testCacheOrInvoke_withLock() = runTest {
        val cacheStore = CoroutineCacheStore(lock = true)

        launch {
            val result1 = cacheStore.cacheOrInvoke {
                delay(100)
                "value1"
            }
            assertEquals("value1", result1)
        }
        launch {
            val result2 = cacheStore.cacheOrInvoke {
                delay(100)
                "value2"
            }
            assertEquals("value1", result2)
        }
    }

    @Test
    fun testCacheOrInvoke_withoutLock() = runTest {
        val cacheStore = CoroutineCacheStore(lock = false)

        launch {
            val result1 = cacheStore.cacheOrInvoke {
                delay(100)
                "value1"
            }
            assertEquals("value1", result1)
        }
        launch {
            val result2 = cacheStore.cacheOrInvoke {
                delay(100)
                "value2"
            }
            assertEquals("value2", result2)
        }
    }
}
