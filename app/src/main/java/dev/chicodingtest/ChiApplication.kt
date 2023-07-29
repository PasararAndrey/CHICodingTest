package dev.chicodingtest

import android.app.Application
import dev.chicodingtest.data.UserDatabase
import dev.chicodingtest.data.UserRepository

class ChiApplication : Application() {
    private val userDatabase by lazy {
        UserDatabase.get(this)
    }
    val repository by lazy { UserRepository(userDatabase.dao) }
}