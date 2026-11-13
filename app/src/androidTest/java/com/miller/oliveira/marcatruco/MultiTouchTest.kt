package com.miller.oliveira.marcatruco

import android.app.Activity
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultiTouchTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private fun assertScore(playerOne: Int, playerTwo: Int) {
        onView(withId(R.id.player_one_current_value)).check(matches(withText(playerOne.toString())))
        onView(withId(R.id.player_two_value)).check(matches(withText(playerTwo.toString())))
    }

    @Test
    fun doisBotoesAoMesmoTempoNaoAcionamNenhum() {
        twoFingerTap(R.id.button_add_twelve_player_one, R.id.button_add_twelve_player_two)

        assertScore(0, 0)
        onView(withId(R.id.button_play_history)).perform(click())
        onView(withId(R.id.player_one_victories)).check(matches(withText("0")))
        onView(withId(R.id.player_two_victories)).check(matches(withText("0")))
    }

    @Test
    fun botoesDeNavegacaoAoMesmoTempoNaoAbremTelas() {
        twoFingerTap(R.id.button_play_history, R.id.button_provide_names)

        onView(withId(R.id.button_add_one_player_one)).check(matches(withText("+1")))
    }

    @Test
    fun toqueNormalFuncionaDepoisDeUmGestoBloqueado() {
        twoFingerTap(R.id.button_add_three_player_one, R.id.button_add_six_player_two)

        onView(withId(R.id.button_add_three_player_one)).perform(click())

        assertScore(3, 0)
    }

    @Test
    fun segundoDedoForaDosBotoesNaoBloqueia() {
        twoFingerTap(R.id.button_add_three_player_one, R.id.logo)

        assertScore(3, 0)
    }

    /** Encosta o primeiro dedo em [firstId], o segundo em [secondId] e solta os dois. */
    private fun twoFingerTap(firstId: Int, secondId: Int) {
        scenarioRule.scenario.onActivity { activity ->
            val first = activity.centerOf(firstId)
            val second = activity.centerOf(secondId)
            val downTime = SystemClock.uptimeMillis()

            val events = listOf(
                event(downTime, MotionEvent.ACTION_DOWN, first),
                event(downTime, pointerAction(MotionEvent.ACTION_POINTER_DOWN), first, second),
                event(downTime, pointerAction(MotionEvent.ACTION_POINTER_UP), first, second),
                event(downTime, MotionEvent.ACTION_UP, first),
            )
            events.forEach {
                activity.dispatchTouchEvent(it)
                it.recycle()
            }
        }
    }

    private fun pointerAction(action: Int) =
        action or (1 shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)

    private fun Activity.centerOf(id: Int): FloatArray {
        val view = findViewById<View>(id)
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return floatArrayOf(location[0] + view.width / 2f, location[1] + view.height / 2f)
    }

    private fun event(downTime: Long, action: Int, vararg points: FloatArray): MotionEvent {
        val properties = Array(points.size) { index ->
            MotionEvent.PointerProperties().apply {
                id = index
                toolType = MotionEvent.TOOL_TYPE_FINGER
            }
        }
        val coords = Array(points.size) { index ->
            MotionEvent.PointerCoords().apply {
                x = points[index][0]
                y = points[index][1]
                pressure = 1f
                size = 1f
            }
        }
        return MotionEvent.obtain(
            downTime, SystemClock.uptimeMillis(), action, points.size, properties, coords,
            0, 0, 1f, 1f, 0, 0, android.view.InputDevice.SOURCE_TOUCHSCREEN, 0,
        )
    }
}
