package com.miller.oliveira.marcatruco

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private fun click(id: Int) = onView(withId(id)).perform(click())

    private fun assertText(id: Int, text: String) =
        onView(withId(id)).check(matches(withText(text)))

    private fun assertScore(playerOne: Int, playerTwo: Int) {
        assertText(R.id.player_one_current_value, playerOne.toString())
        assertText(R.id.player_two_value, playerTwo.toString())
    }

    @Test
    fun telaInicialMostraNomesPadraoEPlacarZerado() {
        assertText(R.id.player_one_title, context.getString(R.string.player_one))
        assertText(R.id.player_two_title, context.getString(R.string.player_two))
        assertScore(0, 0)
    }

    @Test
    fun botoesDoJogadorUmSomamApenasParaEle() {
        click(R.id.button_add_three_player_one)

        assertScore(3, 0)
    }

    @Test
    fun botoesDoJogadorDoisSomamApenasParaEle() {
        click(R.id.button_add_six_player_two)

        assertScore(0, 6)
    }

    @Test
    fun pontosSeAcumulam() {
        click(R.id.button_add_one_player_one)
        click(R.id.button_add_three_player_one)
        click(R.id.button_add_six_player_one)

        assertScore(10, 0)
    }

    @Test
    fun cliqueDuploNoMesmoBotaoContaUmaVez() {
        onView(withId(R.id.button_add_three_player_one)).perform(click(), click())

        assertScore(3, 0)
    }

    @Test
    fun chegarA12ZeraOPlacarDosDois() {
        click(R.id.button_add_three_player_two)
        click(R.id.button_add_nine_player_one)
        click(R.id.button_add_three_player_one)

        assertScore(0, 0)
    }

    @Test
    fun placarENomesSobrevivemARecriacaoDaTela() {
        click(R.id.button_add_nine_player_one)
        click(R.id.button_add_one_player_two)
        renamePlayers("Ana", "Bia")

        click(R.id.button_add_twelve_player_two)
        click(R.id.button_add_three_player_one)

        scenarioRule.scenario.recreate()

        assertScore(3, 0)
        assertText(R.id.player_one_title, "Ana")
        assertText(R.id.player_two_title, "Bia")

        click(R.id.button_play_history)
        assertText(R.id.player_two_victories, "1")
    }

    @Test
    fun nomesInformadosAparecemNaTelaPrincipal() {
        renamePlayers("Ana", "Bia")

        assertText(R.id.player_one_title, "Ana")
        assertText(R.id.player_two_title, "Bia")
    }

    @Test
    fun nomeEmBrancoMantemONomeAtual() {
        renamePlayers("Ana", "   ")

        assertText(R.id.player_one_title, "Ana")
        assertText(R.id.player_two_title, context.getString(R.string.player_two))
    }

    @Test
    fun voltarSemSalvarMantemOsNomes() {
        click(R.id.button_provide_names)
        onView(withId(R.id.player_one_name)).perform(replaceText("Ana"))
        closeSoftKeyboard()
        pressBack()

        assertText(R.id.player_one_title, context.getString(R.string.player_one))
    }

    @Test
    fun historicoMostraNomesEVitorias() {
        renamePlayers("Ana", "Bia")
        click(R.id.button_add_twelve_player_two)

        click(R.id.button_play_history)

        assertText(R.id.player_one_name, "Ana")
        assertText(R.id.player_two_name, "Bia")
        assertText(R.id.player_one_victories, "0")
        assertText(R.id.player_two_victories, "1")
    }

    @Test
    fun zerarHistoricoZeraPlacarVitoriasENomes() {
        renamePlayers("Ana", "Bia")
        click(R.id.button_add_twelve_player_one)
        click(R.id.button_add_six_player_two)

        click(R.id.button_restart_history)
        assertScore(0, 0)
        assertText(R.id.player_one_title, context.getString(R.string.player_one))
        assertText(R.id.player_two_title, context.getString(R.string.player_two))

        click(R.id.button_play_history)
        assertText(R.id.player_one_name, context.getString(R.string.player_one))
        assertText(R.id.player_two_name, context.getString(R.string.player_two))
        assertText(R.id.player_one_victories, "0")
        assertText(R.id.player_two_victories, "0")
    }

    private fun renamePlayers(playerOne: String, playerTwo: String) {
        click(R.id.button_provide_names)
        onView(withId(R.id.player_one_name)).perform(replaceText(playerOne))
        onView(withId(R.id.player_two_name)).perform(replaceText(playerTwo))
        closeSoftKeyboard()
        click(R.id.button)
    }
}
