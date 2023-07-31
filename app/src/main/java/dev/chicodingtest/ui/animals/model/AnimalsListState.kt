package dev.chicodingtest.ui.animals.model

import androidx.paging.PagingData
import dev.chicodingtest.domain.model.Animal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class AnimalsListState(
    val animals: Flow<PagingData<Animal>> = emptyFlow()
)