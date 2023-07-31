package dev.chicodingtest.data.api

import dev.chicodingtest.data.mappers.models.AnimalsDTO
import dev.chicodingtest.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AnimalsRemoteDataSource @Inject constructor(private val animalsApi: AnimalsApi, @IoDispatcher private val ioDispatcher: CoroutineDispatcher) {
    suspend fun fetchAnimalImages(): AnimalsDTO {
        return withContext(ioDispatcher) {
            animalsApi.getRandomAnimalImages()
        }
    }
}