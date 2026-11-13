package com.miller.oliveira.marcatruco

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
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
class ProvideNamesActivityTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun mostraTituloNaToolbar() {
        ActivityScenario.launch(ProvideNamesActivity::class.java).use {
            onView(withText(R.string.provide_names_title)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun salvarDevolveOsNomesSemEspacos() {
        ActivityScenario.launchActivityForResult(ProvideNamesActivity::class.java).use { scenario ->
            onView(withId(R.id.player_one_name)).perform(replaceText("  Ana "))
            onView(withId(R.id.player_two_name)).perform(replaceText("Bia"))
            closeSoftKeyboard()
            onView(withId(R.id.button)).perform(click())

            val result = scenario.result
            assertEquals(Activity.RESULT_OK, result.resultCode)
            assertEquals("Ana", result.resultData.getStringExtra(IntentKeys.PLAYER_ONE_NAME))
            assertEquals("Bia", result.resultData.getStringExtra(IntentKeys.PLAYER_TWO_NAME))
        }
    }

    @Test
    fun salvarComCamposVaziosDevolveTextoVazio() {
        ActivityScenario.launchActivityForResult(ProvideNamesActivity::class.java).use { scenario ->
            closeSoftKeyboard()
            onView(withId(R.id.button)).perform(click())

            val result = scenario.result
            assertEquals(Activity.RESULT_OK, result.resultCode)
            assertEquals("", result.resultData.getStringExtra(IntentKeys.PLAYER_ONE_NAME))
            assertEquals("", result.resultData.getStringExtra(IntentKeys.PLAYER_TWO_NAME))
        }
    }

    @Test
    fun botaoVoltarDaToolbarCancela() {
        ActivityScenario.launchActivityForResult(ProvideNamesActivity::class.java).use { scenario ->
            val upDescription =
                context.getString(androidx.appcompat.R.string.abc_action_bar_up_description)
            onView(withContentDescription(upDescription)).perform(click())

            assertEquals(Activity.RESULT_CANCELED, scenario.result.resultCode)
        }
    }
}
