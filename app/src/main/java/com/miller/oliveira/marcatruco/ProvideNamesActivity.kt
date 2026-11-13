package com.miller.oliveira.marcatruco

import android.content.Intent
import android.os.Bundle
import com.miller.oliveira.marcatruco.databinding.ActivityProvideNamesBinding

class ProvideNamesActivity : SingleTouchActivity() {

    private lateinit var binding: ActivityProvideNamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProvideNamesBinding.inflate(layoutInflater)

        enableEdgeToEdgeWithLightIcons()
        setContentView(binding.root)
        binding.root.applySystemBarsPadding()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.button.setOnSingleClickListener { confirmButtonClick() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun confirmButtonClick() {
        val result = Intent().apply {
            putExtra(IntentKeys.PLAYER_ONE_NAME, binding.playerOneName.text.toString().trim())
            putExtra(IntentKeys.PLAYER_TWO_NAME, binding.playerTwoName.text.toString().trim())
        }

        setResult(RESULT_OK, result)
        finish()
    }
}
