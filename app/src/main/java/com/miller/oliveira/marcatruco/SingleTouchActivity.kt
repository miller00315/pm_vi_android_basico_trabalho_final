package com.miller.oliveira.marcatruco

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity base que impede tocar em dois botões ao mesmo tempo.
 *
 * Se um segundo dedo encostar num botão diferente do primeiro, o gesto inteiro é cancelado
 * (nenhum dos botões é acionado) e um Toast avisa o usuário. Toques com mais de um dedo fora
 * dos botões continuam funcionando normalmente.
 */
open class SingleTouchActivity : AppCompatActivity() {

    private var gestureBlocked = false

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> gestureBlocked = false
            MotionEvent.ACTION_POINTER_DOWN ->
                if (!gestureBlocked && touchesMoreThanOneButton(event)) blockGesture(event)
        }

        if (gestureBlocked) return true
        return super.dispatchTouchEvent(event)
    }

    private fun touchesMoreThanOneButton(event: MotionEvent): Boolean {
        val root = window.decorView
        val touched = (0 until event.pointerCount)
            .mapNotNull { root.findClickableAt(event.getX(it), event.getY(it)) }
            .toSet()
        return touched.size > 1
    }

    private fun blockGesture(event: MotionEvent) {
        gestureBlocked = true

        val cancel = MotionEvent.obtain(event).apply { action = MotionEvent.ACTION_CANCEL }
        super.dispatchTouchEvent(cancel)
        cancel.recycle()

        Toast.makeText(this, R.string.one_button_at_a_time, Toast.LENGTH_SHORT).show()
    }

    /** Componente clicável mais interno sob o ponto ([x], [y]) em coordenadas da janela. */
    private fun View.findClickableAt(x: Float, y: Float): View? {
        if (!isShown) return null

        val location = IntArray(2)
        getLocationInWindow(location)
        val inside = x >= location[0] && x < location[0] + width &&
            y >= location[1] && y < location[1] + height
        if (!inside) return null

        if (this is ViewGroup) {
            for (i in childCount - 1 downTo 0) {
                getChildAt(i).findClickableAt(x, y)?.let { return it }
            }
        }
        return if (isClickable && isEnabled) this else null
    }
}
