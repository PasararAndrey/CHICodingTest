package dev.chicodingtest.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
object DispatchersModule {

    @MainDispatcher
    @Provides
    @AppScope
    fun provideDispatcherMain(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    @AppScope
    fun provideDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

    @IoDispatcher
    @Provides
    @AppScope
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

