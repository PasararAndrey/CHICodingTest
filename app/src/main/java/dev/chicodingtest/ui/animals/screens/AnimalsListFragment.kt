package dev.chicodingtest.ui.animals.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dev.chicodingtest.R
import dev.chicodingtest.appComponent
import dev.chicodingtest.databinding.FragmentAnimalsListBinding
import dev.chicodingtest.ui.animals.AnimalsViewModel
import dev.chicodingtest.ui.animals.adapters.AnimalsListAdapter
import dev.chicodingtest.ui.animals.adapters.ListItemDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimalsListFragment : Fragment(R.layout.fragment_animals_list) {

    private var _binding: FragmentAnimalsListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: AnimalsViewModel.Factory
    private val viewModel by activityViewModels<AnimalsViewModel> { viewModelFactory }
    private var adapter: AnimalsListAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAnimalsListBinding.bind(view)
        setupAnimalsList()
        observeListUpdates()
    }

    private fun observeListUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.animalsListState.collect { uiState ->
                    Log.d(TAG, "onViewCreated: got list ${uiState.animals}")
                    uiState.animals.collectLatest { animalPagingData ->
                        adapter?.submitData(animalPagingData)
                    }
                }
            }
        }
    }

    private fun setupAnimalsList() {
        adapter = AnimalsListAdapter { animal ->
            viewModel.toggleFavoriteStatus(animal)
        }
        binding.animalsList.apply {
            adapter = this@AnimalsListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ListItemDecorator(16, 64, 16))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

    companion object {
        private const val TAG = "AnimalsFragment"
    }
}