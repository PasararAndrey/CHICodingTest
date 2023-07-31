package dev.chicodingtest.di

import dagger.Component
import dev.chicodingtest.ui.animals.screens.AnimalsListFragment
import dev.chicodingtest.ui.animals.screens.FavoriteAnimalsFragment
import javax.inject.Scope

@[AppScope Component(modules = [NetworkModule::class, DispatchersModule::class])]
interface AppComponent {
    fun inject(fragment: AnimalsListFragment)
    fun inject(fragment: FavoriteAnimalsFragment)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}

@Scope
annotation class AppScope