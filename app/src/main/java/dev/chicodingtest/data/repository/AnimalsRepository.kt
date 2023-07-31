package dev.chicodingtest.data.repository

import dev.chicodingtest.data.api.AnimalsRemoteDataSource
import dev.chicodingtest.data.mappers.toAnimalModel
import dev.chicodingtest.domain.model.Animal
import javax.inject.Inject

class AnimalsRepository @Inject constructor(private val animalsRemoteDataSource: AnimalsRemoteDataSource) {
    suspend fun fetchAnimals(): List<Animal> = animalsRemoteDataSource.fetchAnimalImages().toAnimalModel()
}