package dev.chicodingtest

import android.os.Bundle
import android.util.Log
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COUNTER_STATE, counter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        counter = savedInstanceState.getInt(COUNTER_STATE, 0)
        Log.d(TAG, "Restored value of counter:$counter")
        setCounter()
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun setupUI() {
        binding.tvCounter.text = "0"
        binding.btnIncrement.setOnClickListener {
            incrementAndSetCounter()
        }
    }

    private fun incrementAndSetCounter() {
        counter++
        Log.d(TAG, "Counter value:$counter")
        setCounter()
    }

    private fun setCounter() {
        binding.tvCounter.text = counter.toString()
    }


    companion object {
        private const val COUNTER_STATE = "counter_state"
        private const val TAG = "main_activity"
    }
}