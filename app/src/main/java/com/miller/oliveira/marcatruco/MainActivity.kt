package com.miller.oliveira.marcatruco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.miller.oliveira.marcatruco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val KEY_PLAYER_ONE_NAME = "PLAYER_ONE_NAME"
        private const val KEY_PLAYER_TWO_NAME = "PLAYER_TWO_NAME"
        private const val KEY_PLAYER_ONE_POINT = "PLAYER_ONE_POINT"
        private const val KEY_PLAYER_TWO_POINT = "PLAYER_TWO_POINT"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var playerOneName: String
    private lateinit var playerTwoName: String

    private var playerOnePoint = 0
    private var playerTwoPoint = 0

    private val providerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                print("")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        restoreState(savedInstanceState)
        updateScreen()

        setupNavigationButtons()
        setupAddPointButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(KEY_PLAYER_ONE_NAME, playerOneName)
        outState.putString(KEY_PLAYER_TWO_NAME, playerTwoName)
        outState.putInt(KEY_PLAYER_ONE_POINT, playerOnePoint)
        outState.putInt(KEY_PLAYER_TWO_POINT, playerTwoPoint)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        playerOneName = savedInstanceState?.getString(KEY_PLAYER_ONE_NAME)
            ?: getString(R.string.player_one)
        playerTwoName = savedInstanceState?.getString(KEY_PLAYER_TWO_NAME)
            ?: getString(R.string.player_two)
        playerOnePoint = savedInstanceState?.getInt(KEY_PLAYER_ONE_POINT) ?: 0
        playerTwoPoint = savedInstanceState?.getInt(KEY_PLAYER_TWO_POINT) ?: 0
    }

    private fun updateScreen() {
        binding.playerOneTitle.text = playerOneName
        binding.playerTwoTitle.text = playerTwoName
        binding.playerOneCurrentValue.text = playerOnePoint.toString()
        binding.playerTwoValue.text = playerTwoPoint.toString()
    }

    private fun setupNavigationButtons() {
        binding.buttonProvideNames.setOnClickListener { provideNameOnClick() }
        binding.buttonRestartHistory.setOnClickListener { restartHistoryOnClick() }
        binding.buttonPlayHistory.setOnClickListener { playHistoryOnClick() }
    }

    private fun setupAddPointButtons() {
        val buttons = mapOf(
            binding.buttonAddOnePlayerOne to (1 to 1),
            binding.buttonAddThreePlayerOne to (1 to 3),
            binding.buttonAddSixPlayerOne to (1 to 6),
            binding.buttonAddNinePlayerOne to (1 to 9),
            binding.buttonAddTwelvePlayerOne to (1 to 12),
            binding.buttonAddOnePlayerTwo to (2 to 1),
            binding.buttonAddThreePlayerTwo to (2 to 3),
            binding.buttonAddSixPlayerTwo to (2 to 6),
            binding.buttonAddNinePlayerTwo to (2 to 9),
            binding.buttonAddTwelvePlayerTwo to (2 to 12),
        )

        buttons.forEach { (button, value) ->
            val (player, points) = value
            button.setOnClickListener { addPointOnClick(player, points) }
        }
    }

    private fun addPointOnClick(player: Int, points: Int) {
        if (player == 1) playerOnePoint += points else playerTwoPoint += points
        updateScreen()
    }

    private fun playHistoryOnClick() {
        startActivity(Intent(this, PlayHistoryActivity::class.java))
    }

    private fun restartHistoryOnClick() {
    }

    private fun provideNameOnClick() {
        providerLauncher.launch(Intent(this, ProvideNamesActivity::class.java))
    }
}
