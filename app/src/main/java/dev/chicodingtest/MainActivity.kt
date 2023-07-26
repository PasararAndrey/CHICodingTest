package dev.chicodingtest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import dev.chicodingtest.Counter.CREATOR.COUNTER_BUNDLE_KEY
import dev.chicodingtest.Counter.CREATOR.COUNTER_REQUEST_KEY
import dev.chicodingtest.databinding.ActivityMainBinding
import dev.chicodingtest.extension.parcelable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        fragmentCounterResult()
    }

    private fun fragmentCounterResult() {
        supportFragmentManager.setFragmentResultListener(COUNTER_REQUEST_KEY, this) { _, result: Bundle ->
            val counter = result.parcelable(COUNTER_BUNDLE_KEY) ?: Counter()
            Log.d(TAG, "Got counter value: ${counter.countValue}")
            binding.tvCounter.text = counter.countValue.toString()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(COUNTER_BUNDLE_KEY, binding.tvCounter.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        binding.tvCounter.text = savedInstanceState.getString(COUNTER_BUNDLE_KEY) ?: "0"
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun setupUI() {
        binding.tvCounter.text = "0"
        binding.btnMoveToCounter.setOnClickListener {
            moveToCounterFragment()
        }
    }

    private fun moveToCounterFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<CounterFragment>(R.id.fragment_container)
            addToBackStack("counter_fragment")
        }
    }

    companion object {
        private const val TAG = "main_activity"
    }
}