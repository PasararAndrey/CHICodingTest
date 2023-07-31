package dev.chicodingtest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.chicodingtest.data.api.AnimalsPagingSource
import dev.chicodingtest.data.api.AnimalsRemoteDataSource
import dev.chicodingtest.domain.model.Animal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimalsRepository @Inject constructor(private val animalsRemoteDataSource: AnimalsRemoteDataSource) {
    fun fetchAnimals(): Flow<PagingData<Animal>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                AnimalsPagingSource(animalsRemoteDataSource)
            },
        ).flow
    }
}