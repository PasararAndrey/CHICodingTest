package dev.chicodingtest.data.api

import dev.chicodingtest.data.mappers.models.AnimalsDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalsApi {

    @GET("shibes")
    suspend fun getRandomAnimalImages(
        @Query("count") count: Int = 10,
        @Query("urls") urls: Boolean = true,
        @Query("httpsUrls") httpsUrls: Boolean = true
    ): AnimalsDTO
}