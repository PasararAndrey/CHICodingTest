package dev.chicodingtest.data.mappers

import dev.chicodingtest.data.mappers.models.AnimalsDTO
import dev.chicodingtest.domain.model.Animal

fun AnimalsDTO.toAnimalModel(): List<Animal> {
    return this.map { url ->
        Animal(url)
    }
}