package com.miller.oliveira.marcatruco

import android.os.SystemClock
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Soma os insets das barras do sistema ao padding definido no XML,
 * em vez de sobrescrevê-lo.
 */
fun View.applySystemBarsPadding() {
    val initialLeft = paddingLeft
    val initialTop = paddingTop
    val initialRight = paddingRight
    val initialBottom = paddingBottom

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(
            initialLeft + bars.left,
            initialTop + bars.top,
            initialRight + bars.right,
            initialBottom + bars.bottom
        )
        insets
    }
}

private const val DEFAULT_CLICK_INTERVAL_MS = 600L

/**
 * Igual ao setOnClickListener, mas ignora cliques repetidos dentro de [intervalMs]
 * (evita abrir a mesma tela duas vezes ou somar pontos em dobro).
 */
fun View.setOnSingleClickListener(
    intervalMs: Long = DEFAULT_CLICK_INTERVAL_MS,
    onClick: (View) -> Unit,
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime < intervalMs) return@setOnClickListener
        lastClickTime = now
        onClick(view)
    }
}
