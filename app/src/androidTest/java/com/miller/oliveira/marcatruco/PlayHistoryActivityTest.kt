package com.miller.oliveira.marcatruco

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayHistoryActivityTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private fun assertText(id: Int, text: String) =
        onView(withId(id)).check(matches(withText(text)))

    @Test
    fun mostraNomesEVitoriasRecebidos() {
        val intent = Intent(context, PlayHistoryActivity::class.java).apply {
            putExtra(IntentKeys.PLAYER_ONE_NAME, "Ana")
            putExtra(IntentKeys.PLAYER_TWO_NAME, "Bia")
            putExtra(IntentKeys.PLAYER_ONE_VICTORIES, 3)
            putExtra(IntentKeys.PLAYER_TWO_VICTORIES, 5)
        }

        ActivityScenario.launch<PlayHistoryActivity>(intent).use {
            assertText(R.id.player_one_name, "Ana")
            assertText(R.id.player_two_name, "Bia")
            assertText(R.id.player_one_victories, "3")
            assertText(R.id.player_two_victories, "5")
        }
    }

    @Test
    fun semExtrasMostraNomesPadraoEZeroVitorias() {
        ActivityScenario.launch(PlayHistoryActivity::class.java).use {
            assertText(R.id.player_one_name, context.getString(R.string.player_one))
            assertText(R.id.player_two_name, context.getString(R.string.player_two))
            assertText(R.id.player_one_victories, "0")
            assertText(R.id.player_two_victories, "0")
        }
    }

    @Test
    fun mostraTituloNaToolbar() {
        ActivityScenario.launch(PlayHistoryActivity::class.java).use {
            onView(withText(R.string.play_history_title)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun botaoVoltarDaToolbarFechaATela() {
        ActivityScenario.launchActivityForResult(PlayHistoryActivity::class.java).use { scenario ->
            val upDescription =
                context.getString(androidx.appcompat.R.string.abc_action_bar_up_description)
            onView(withContentDescription(upDescription)).perform(click())

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
        }
    }

    @Test
    fun dadosSobrevivemARecriacaoDaTela() {
        val intent = Intent(context, PlayHistoryActivity::class.java).apply {
            putExtra(IntentKeys.PLAYER_ONE_NAME, "Ana")
            putExtra(IntentKeys.PLAYER_TWO_VICTORIES, 4)
        }

        ActivityScenario.launch<PlayHistoryActivity>(intent).use { scenario ->
            scenario.recreate()

            assertText(R.id.player_one_name, "Ana")
            assertText(R.id.player_two_victories, "4")
        }
    }
}
