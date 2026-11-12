package com.miller.oliveira.marcatruco

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.miller.oliveira.marcatruco.databinding.ActivityPlayHistoryBinding

class PlayHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayHistoryBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(R.layout.activity_play_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}