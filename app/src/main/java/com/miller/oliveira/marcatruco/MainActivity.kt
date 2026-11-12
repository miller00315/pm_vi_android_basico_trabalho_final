package com.miller.oliveira.marcatruco

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.miller.oliveira.marcatruco.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var playerTwo: String
    private lateinit var playerOne: String

    private lateinit var binding: ActivityMainBinding

    private val providerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
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

        binding.buttonProvideNames.setOnClickListener {
            provideNameOnClick()
        }

        binding.buttonRestartHistory.setOnClickListener {
            restartHistoryOnClick()
        }

        binding.buttonPlayHistory.setOnClickListener {
            playHistoryOnClick()
        }

        binding.buttonAddOnePlayerOne.setOnClickListener {
            addPointOnClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        providerLauncher.unregister()
    }

    private fun addPointOnClick() {}

    private fun playHistoryOnClick() {
        val intent = Intent(this, PlayHistoryActivity::class.java)

        startActivity(intent)
    }

    private fun restartHistoryOnClick() {

    }

    private fun provideNameOnClick() {
        val intent = Intent(this, ProvideNamesActivity::class.java)

        startActivity(intent)
    }
}