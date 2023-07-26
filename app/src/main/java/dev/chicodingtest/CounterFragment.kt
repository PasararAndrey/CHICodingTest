package dev.chicodingtest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import dev.chicodingtest.Counter.CREATOR.COUNTER_BUNDLE_KEY
import dev.chicodingtest.Counter.CREATOR.COUNTER_REQUEST_KEY
import dev.chicodingtest.databinding.FragmentCounterBinding
import dev.chicodingtest.extension.parcelable

class CounterFragment : Fragment(R.layout.fragment_counter) {

    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!
    private var counter: Counter = Counter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counter = savedInstanceState?.parcelable(COUNTER_BUNDLE_KEY) ?: Counter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCounterBinding.bind(view)
        setupActionBar()
        setCounter()
        sendCounterFragmentResult()
        binding.btnIncrement.setOnClickListener {
            incrementAndSetCounter()
            sendCounterFragmentResult()
        }
    }

    private fun sendCounterFragmentResult() {
        setFragmentResult(COUNTER_REQUEST_KEY, bundleOf(COUNTER_BUNDLE_KEY to counter))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(COUNTER_BUNDLE_KEY, counter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = String()
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun incrementAndSetCounter() {
        counter.increment()
        Log.d(TAG, "Counter value:${counter.countValue}")
        setCounter()
    }

    private fun setCounter() {
        binding.tvCounter.text = counter.countValue.toString()
    }

    companion object {
        private const val TAG = "counter_fragment"
    }

}