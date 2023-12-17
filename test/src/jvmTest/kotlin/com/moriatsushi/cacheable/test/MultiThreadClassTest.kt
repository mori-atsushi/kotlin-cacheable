package com.moriatsushi.cacheable.test

import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MultiThreadClassTest {
    @Test
    fun testWithLock() {
        runBlocking {
            val multiThreadClass = MultiThreadClass()
            launch(Dispatchers.Default) {
                assertEquals(1, multiThreadClass.withLock())
            }
            launch(Dispatchers.Default) {
                assertEquals(1, multiThreadClass.withLock())
            }
        }
    }

    @Test
    fun testWithoutLock() {
        runBlocking {
            val multiThreadClass = MultiThreadClass()
            launch(Dispatchers.Default) {
                assertEquals(1, multiThreadClass.withoutLock())
            }
            delay(10)
            launch(Dispatchers.Default) {
                assertEquals(2, multiThreadClass.withoutLock())
            }
        }
    }
}
