package ru.spectator.bt.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.spectator.bt.databinding.AppActivityBinding


class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AppActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}





















