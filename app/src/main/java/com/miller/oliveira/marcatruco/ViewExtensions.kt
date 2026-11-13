package com.miller.oliveira.marcatruco

import android.graphics.Color
import android.os.SystemClock
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
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
    val throttle = ClickThrottle(intervalMs, SystemClock::elapsedRealtime)

    setOnClickListener { view ->
        if (throttle.tryAcquire()) onClick(view)
    }
}

/**
 * Edge-to-edge com ícones claros nas barras do sistema, já que o fundo do app é escuro (feltro).
 */
fun ComponentActivity.enableEdgeToEdgeWithLightIcons() {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
    )
}

private const val BUMP_SCALE = 1.35f
private const val BUMP_DURATION_MS = 350L

/**
 * Troca o texto e, se ele mudou, dá um "pulinho" no TextView.
 */
fun TextView.setTextWithBump(newText: CharSequence) {
    if (text.toString() == newText.toString()) return

    text = newText
    animate().cancel()
    scaleX = BUMP_SCALE
    scaleY = BUMP_SCALE
    animate()
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(BUMP_DURATION_MS)
        .setInterpolator(OvershootInterpolator(3f))
        .start()
}
