package dev.chicodingtest

import android.app.Application
import dev.chicodingtest.database.UserDatabase
import dev.chicodingtest.database.UserRepository

class ChiApplication : Application() {
    private val userDatabase by lazy {
        UserDatabase.get(this)
    }
    val repository by lazy { UserRepository(userDatabase.dao) }
}