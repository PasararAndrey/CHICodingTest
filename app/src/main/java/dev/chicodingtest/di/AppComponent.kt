package dev.chicodingtest.di

import dagger.Component
import dev.chicodingtest.ui.animals.AnimalsFragment
import javax.inject.Scope

@[AppScope Component(modules = [NetworkModule::class, DispatchersModule::class])]
interface AppComponent {
    fun inject(animalsFragment: AnimalsFragment)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}

@Scope
annotation class AppScope