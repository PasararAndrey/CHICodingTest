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
import dev.chicodingtest.databinding.FragmentFavoriteAnimalsBinding
import dev.chicodingtest.ui.animals.AnimalsViewModel
import dev.chicodingtest.ui.animals.adapters.FavoriteAnimalsAdapter
import dev.chicodingtest.ui.animals.adapters.ListItemDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteAnimalsFragment : Fragment(R.layout.fragment_favorite_animals) {

    private var _binding: FragmentFavoriteAnimalsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var viewModelFactory: AnimalsViewModel.Factory
    private val viewModel by activityViewModels<AnimalsViewModel> { viewModelFactory }
    private var adapter: FavoriteAnimalsAdapter = FavoriteAnimalsAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteAnimalsBinding.bind(view)
        setupList()
        observeFavoriteAnimals()
    }

    private fun setupList() {
        binding.favoriteAnimalsList.apply {
            adapter = this@FavoriteAnimalsFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(ListItemDecorator(16, 64, 32))
        }
    }

    private fun observeFavoriteAnimals() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.favoriteAnimalsList.collectLatest { newAnimalsList ->
                        Log.d(TAG, "Collected: $newAnimalsList")
                        adapter.submitList(newAnimalsList)
                        binding.favoriteAnimalsList.invalidateItemDecorations()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "FavoriteAnimalsFragment"
    }
}
