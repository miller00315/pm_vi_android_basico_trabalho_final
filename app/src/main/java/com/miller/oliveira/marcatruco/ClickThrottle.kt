package com.miller.oliveira.marcatruco

/**
 * Libera no máximo um clique a cada [intervalMs].
 * O relógio é injetável para permitir testes sem Android.
 */
class ClickThrottle(
    private val intervalMs: Long,
    private val clock: () -> Long,
) {
    private var lastClickTime: Long? = null

    fun tryAcquire(): Boolean {
        val now = clock()
        val last = lastClickTime
        if (last != null && now - last < intervalMs) return false

        lastClickTime = now
        return true
    }
}
