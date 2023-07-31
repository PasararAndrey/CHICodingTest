package dev.chicodingtest.ui.animals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.chicodingtest.data.repository.AnimalsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AnimalsViewModel(private val animalsRepository: AnimalsRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<AnimalsUiState> = MutableStateFlow(AnimalsUiState())
    val uiState: StateFlow<AnimalsUiState> = _uiState
    private var fetchAnimalsJob: Job? = null

    fun fetchAnimals() {
        fetchAnimalsJob?.cancel()
        fetchAnimalsJob = viewModelScope.launch {
            val fetchedAnimals = animalsRepository.fetchAnimals()
            _uiState.update { animalsUiState ->
                animalsUiState.copy(animals = fetchedAnimals)
            }
        }
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