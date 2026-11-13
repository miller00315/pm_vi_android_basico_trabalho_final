package com.miller.oliveira.marcatruco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.miller.oliveira.marcatruco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WINNING_SCORE = 12

        private const val KEY_NAME = "NAME_"
        private const val KEY_POINTS = "POINTS_"
        private const val KEY_VICTORIES = "VICTORIES_"
    }

    private class Player(var name: String) {
        var points = 0
        var victories = 0
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player
    private val players get() = listOf(playerOne, playerTwo)

    private val providerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            result.data?.let { data ->
                updateName(playerOne, data.getStringExtra(IntentKeys.PLAYER_ONE_NAME))
                updateName(playerTwo, data.getStringExtra(IntentKeys.PLAYER_TWO_NAME))
                updateScreen()
            }
        }

    private fun updateName(player: Player, name: String?) {
        if (!name.isNullOrBlank()) player.name = name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        restoreState(savedInstanceState)
        updateScreen()

        setupNavigationButtons()
        setupAddPointButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        players.forEachIndexed { index, player ->
            outState.putString(KEY_NAME + index, player.name)
            outState.putInt(KEY_POINTS + index, player.points)
            outState.putInt(KEY_VICTORIES + index, player.victories)
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        playerOne = restorePlayer(savedInstanceState, 0, getString(R.string.player_one))
        playerTwo = restorePlayer(savedInstanceState, 1, getString(R.string.player_two))
    }

    private fun restorePlayer(state: Bundle?, index: Int, defaultName: String) =
        Player(state?.getString(KEY_NAME + index) ?: defaultName).apply {
            points = state?.getInt(KEY_POINTS + index) ?: 0
            victories = state?.getInt(KEY_VICTORIES + index) ?: 0
        }

    private fun updateScreen() = with(binding) {
        playerOneTitle.text = playerOne.name
        playerTwoTitle.text = playerTwo.name
        playerOneCurrentValue.text = playerOne.points.toString()
        playerTwoValue.text = playerTwo.points.toString()
    }

    private fun setupNavigationButtons() = with(binding) {
        buttonProvideNames.setOnSingleClickListener { provideNameOnClick() }
        buttonRestartHistory.setOnSingleClickListener { restartHistoryOnClick() }
        buttonPlayHistory.setOnSingleClickListener { playHistoryOnClick() }
    }

    private fun setupAddPointButtons() = with(binding) {
        bindPointButtons(
            playerOne,
            buttonAddOnePlayerOne to 1,
            buttonAddThreePlayerOne to 3,
            buttonAddSixPlayerOne to 6,
            buttonAddNinePlayerOne to 9,
            buttonAddTwelvePlayerOne to 12,
        )
        bindPointButtons(
            playerTwo,
            buttonAddOnePlayerTwo to 1,
            buttonAddThreePlayerTwo to 3,
            buttonAddSixPlayerTwo to 6,
            buttonAddNinePlayerTwo to 9,
            buttonAddTwelvePlayerTwo to 12,
        )
    }

    private fun bindPointButtons(player: Player, vararg buttons: Pair<Button, Int>) {
        buttons.forEach { (button, points) ->
            button.setOnSingleClickListener { addPoints(player, points) }
        }
    }

    private fun addPoints(player: Player, points: Int) {
        player.points += points

        if (player.points >= WINNING_SCORE) {
            player.victories++
            resetPoints()
            Toast.makeText(this, getString(R.string.player_won, player.name), Toast.LENGTH_SHORT)
                .show()
        }

        updateScreen()
    }

    private fun resetPoints() {
        players.forEach { it.points = 0 }
    }

    private fun playHistoryOnClick() {
        val intent = Intent(this, PlayHistoryActivity::class.java).apply {
            putExtra(IntentKeys.PLAYER_ONE_NAME, playerOne.name)
            putExtra(IntentKeys.PLAYER_TWO_NAME, playerTwo.name)
            putExtra(IntentKeys.PLAYER_ONE_VICTORIES, playerOne.victories)
            putExtra(IntentKeys.PLAYER_TWO_VICTORIES, playerTwo.victories)
        }
        startActivity(intent)
    }

    private fun restartHistoryOnClick() {
        players.forEach { it.victories = 0 }
        resetPoints()
        updateScreen()
    }

    private fun provideNameOnClick() {
        providerLauncher.launch(Intent(this, ProvideNamesActivity::class.java))
    }
}
