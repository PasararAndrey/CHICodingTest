package dev.chicodingtest

import android.app.Application
import android.content.Context
import dev.chicodingtest.di.AppComponent
import dev.chicodingtest.di.DaggerAppComponent

class CodingTestApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is CodingTestApp -> appComponent
        else -> this.applicationContext.appComponent
    }