package dev.chicodingtest.ui.animals

import dev.chicodingtest.domain.model.Animal

data class AnimalsUiState(
    val animals: List<Animal> = emptyList()
)