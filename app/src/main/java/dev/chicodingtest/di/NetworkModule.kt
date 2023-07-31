package dev.chicodingtest.di

import dagger.Module
import dagger.Provides
import dev.chicodingtest.data.api.AnimalsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    @Provides
    @AppScope
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
    }

    @Provides
    @AppScope
    fun provideGson(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @AppScope
    fun provideAnimalsApi(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): AnimalsApi = Retrofit.Builder()
        .baseUrl("https://shibe.online/api/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory).build()
        .create(AnimalsApi::class.java)
}