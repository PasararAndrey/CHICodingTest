package dev.chicodingtest.ui.animals

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.chicodingtest.data.repository.AnimalsRepository
import dev.chicodingtest.domain.model.Animal
import dev.chicodingtest.ui.animals.model.AnimalsListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimalsViewModel(private val animalsRepository: AnimalsRepository) : ViewModel() {

    private val _animalsListState: MutableStateFlow<AnimalsListState> = MutableStateFlow(AnimalsListState())
    val animalsListState: StateFlow<AnimalsListState> = _animalsListState
    private var fetchAnimalsJob: Job? = null
    private val _favoriteAnimalsList: MutableStateFlow<List<Animal>> = MutableStateFlow(listOf())
    val favoriteAnimalsList: StateFlow<List<Animal>> = _favoriteAnimalsList

    init {
        fetchAnimals()
    }

    private fun fetchAnimals() {
        fetchAnimalsJob?.cancel()
        fetchAnimalsJob = viewModelScope.launch {
            val fetchedAnimals: Flow<PagingData<Animal>> = animalsRepository.fetchAnimals().cachedIn(viewModelScope)
            _animalsListState.update { animalsUiState ->
                animalsUiState.copy(animals = fetchedAnimals)
            }
        }
    }

    fun toggleFavoriteStatus(animal: Animal) {
        if (animal.isFavorite) {
            addToFavoriteAnimals(animal)
        } else {
            deleteFromFavoriteAnimals(animal)
        }
        Log.d(TAG, "updatedFavorite: ${favoriteAnimalsList.value} ")
    }

    private fun deleteFromFavoriteAnimals(animal: Animal) {
        viewModelScope.launch {
            _favoriteAnimalsList.update { animals ->
                val newAnimals = animals.toMutableList().also {
                    it.remove(animal)
                }.toList()
                newAnimals
            }
        }
    }

    private fun addToFavoriteAnimals(animal: Animal) {
        viewModelScope.launch {
            _favoriteAnimalsList.update { animals ->
                val newAnimals = animals.toMutableList().also {
                    it.add(animal)
                }.toList()
                newAnimals
            }
        }
    }

    private companion object {
        private const val TAG = "AnimalsViewModel"
    }

    class Factory @Inject constructor(
        private val animalsRepository: AnimalsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AnimalsViewModel::class.java)) {
                return AnimalsViewModel(animalsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}