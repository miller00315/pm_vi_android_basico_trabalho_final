package com.miller.oliveira.marcatruco

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.miller.oliveira.marcatruco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_NAME = "NAME_"
        private const val KEY_POINTS = "POINTS_"
        private const val KEY_VICTORIES = "VICTORIES_"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var game: TrucoGame

    private val providerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult

            result.data?.let { data ->
                game.rename(game.playerOne, data.getStringExtra(IntentKeys.PLAYER_ONE_NAME))
                game.rename(game.playerTwo, data.getStringExtra(IntentKeys.PLAYER_TWO_NAME))
                updateScreen()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdgeWithLightIcons()
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        restoreState(savedInstanceState)
        updateScreen()

        setupNavigationButtons()
        setupAddPointButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        game.players.forEachIndexed { index, player ->
            outState.putString(KEY_NAME + index, player.name)
            outState.putInt(KEY_POINTS + index, player.points)
            outState.putInt(KEY_VICTORIES + index, player.victories)
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        game = TrucoGame(
            restorePlayer(savedInstanceState, 0, getString(R.string.player_one)),
            restorePlayer(savedInstanceState, 1, getString(R.string.player_two)),
        )
    }

    private fun restorePlayer(state: Bundle?, index: Int, defaultName: String) = Player(
        defaultName = defaultName,
        name = state?.getString(KEY_NAME + index) ?: defaultName,
        points = state?.getInt(KEY_POINTS + index) ?: 0,
        victories = state?.getInt(KEY_VICTORIES + index) ?: 0,
    )

    private fun updateScreen() = with(binding) {
        playerOneTitle.text = game.playerOne.name
        playerTwoTitle.text = game.playerTwo.name
        playerOneCurrentValue.setTextWithBump(game.playerOne.points.toString())
        playerTwoValue.setTextWithBump(game.playerTwo.points.toString())
        playerOneProgress.setProgressCompat(game.playerOne.points, true)
        playerTwoProgress.setProgressCompat(game.playerTwo.points, true)
    }

    private fun setupNavigationButtons() = with(binding) {
        buttonProvideNames.setOnSingleClickListener { provideNameOnClick() }
        buttonRestartHistory.setOnSingleClickListener { restartHistoryOnClick() }
        buttonPlayHistory.setOnSingleClickListener { playHistoryOnClick() }
    }

    private fun setupAddPointButtons() = with(binding) {
        bindPointButtons(
            game.playerOne,
            buttonAddOnePlayerOne to 1,
            buttonAddThreePlayerOne to 3,
            buttonAddSixPlayerOne to 6,
            buttonAddNinePlayerOne to 9,
            buttonAddTwelvePlayerOne to 12,
        )
        bindPointButtons(
            game.playerTwo,
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
        game.addPoints(player, points)?.let { winner ->
            Toast.makeText(this, getString(R.string.player_won, winner.name), Toast.LENGTH_SHORT)
                .show()
        }
        updateScreen()
    }

    private fun playHistoryOnClick() {
        val intent = Intent(this, PlayHistoryActivity::class.java).apply {
            putExtra(IntentKeys.PLAYER_ONE_NAME, game.playerOne.name)
            putExtra(IntentKeys.PLAYER_TWO_NAME, game.playerTwo.name)
            putExtra(IntentKeys.PLAYER_ONE_VICTORIES, game.playerOne.victories)
            putExtra(IntentKeys.PLAYER_TWO_VICTORIES, game.playerTwo.victories)
        }
        startActivity(intent)
    }

    private fun restartHistoryOnClick() {
        game.restartHistory()
        updateScreen()
        Toast.makeText(this, R.string.history_restarted, Toast.LENGTH_SHORT).show()
    }

    private fun provideNameOnClick() {
        providerLauncher.launch(Intent(this, ProvideNamesActivity::class.java))
    }
}
