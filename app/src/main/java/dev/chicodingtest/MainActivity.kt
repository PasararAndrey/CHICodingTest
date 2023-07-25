package dev.chicodingtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.chicodingtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.tvCounter.text = "0"
        binding.btnIncrement.setOnClickListener {
            incrementCounter()
        }
    }

    private fun incrementCounter() {
        counter++
        binding.tvCounter.text = counter.toString()
    }
}