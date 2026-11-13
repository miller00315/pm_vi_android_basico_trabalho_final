package com.miller.oliveira.marcatruco

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.miller.oliveira.marcatruco.databinding.ActivityPlayHistoryBinding

class PlayHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayHistoryBinding.inflate(layoutInflater)

        enableEdgeToEdgeWithLightIcons()
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showHistory()
    }

    private fun showHistory() = with(binding) {
        intent.getStringExtra(IntentKeys.PLAYER_ONE_NAME)?.let { playerOneName.text = it }
        intent.getStringExtra(IntentKeys.PLAYER_TWO_NAME)?.let { playerTwoName.text = it }
        playerOneVictories.text = intent.getIntExtra(IntentKeys.PLAYER_ONE_VICTORIES, 0).toString()
        playerTwoVictories.text = intent.getIntExtra(IntentKeys.PLAYER_TWO_VICTORIES, 0).toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
