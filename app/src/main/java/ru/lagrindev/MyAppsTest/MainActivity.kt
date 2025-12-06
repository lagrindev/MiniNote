package ru.lagrindev.MyAppsTest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.lagrindev.MyAppsTest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val text = binding.noteInput.text.toString()
            binding.noteOutput.text = text
        }
    }
}