package dev.chicodingtest.ui.animals

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chicodingtest.R
import dev.chicodingtest.appComponent
import dev.chicodingtest.databinding.FragmentAnimalsBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimalsFragment : Fragment(R.layout.fragment_animals) {

    private var _binding: FragmentAnimalsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: AnimalsViewModel.Factory
    private val viewModel by viewModels<AnimalsViewModel> { viewModelFactory }
    private var adapter: AnimalsAdapter = AnimalsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnimalsBinding.bind(view)
        binding.animalsList.apply {
            adapter = this@AnimalsFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.fetchAnimals()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    Log.d(TAG, "onViewCreated: got list ${uiState.animals}")
                    adapter.animals = uiState.animals
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AnimalsFragment"
    }
}