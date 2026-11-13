package com.miller.oliveira.marcatruco

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ClickThrottleTest {

    private var now = 0L
    private lateinit var throttle: ClickThrottle

    @Before
    fun setUp() {
        now = 0L
        throttle = ClickThrottle(intervalMs = 600) { now }
    }

    @Test
    fun `primeiro clique e liberado mesmo com relogio em zero`() {
        assertTrue(throttle.tryAcquire())
    }

    @Test
    fun `clique dentro do intervalo e bloqueado`() {
        throttle.tryAcquire()

        now = 599
        assertFalse(throttle.tryAcquire())
    }

    @Test
    fun `clique exatamente no fim do intervalo e liberado`() {
        throttle.tryAcquire()

        now = 600
        assertTrue(throttle.tryAcquire())
    }

    @Test
    fun `clique bloqueado nao reinicia o intervalo`() {
        throttle.tryAcquire()

        now = 500
        assertFalse(throttle.tryAcquire())

        now = 600
        assertTrue(throttle.tryAcquire())
    }

    @Test
    fun `intervalo conta a partir do ultimo clique liberado`() {
        throttle.tryAcquire()

        now = 1_000
        assertTrue(throttle.tryAcquire())

        now = 1_500
        assertFalse(throttle.tryAcquire())
    }
}
