package dev.chicodingtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.chicodingtest.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class], version = 1, exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null
        fun get(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val scope = CoroutineScope(Dispatchers.IO)
                val instance: UserDatabase = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java, "user_database"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            scope.launch {
                                listOf(
                                    User(1, "Kolya", 23),
                                    User(2, "Lena", 24),
                                    User(3, "Tanya", 25),
                                    User(4, "Kirill", 26),
                                    User(5, "Artyom", 27),
                                    User(6, "Alexey", 28),
                                    User(7, "Kirill", 29),
                                ).forEach { user ->
                                    database.dao.insertUser(user)
                                }
                            }
                        }
                    }
                }).build().also { INSTANCE = it }
                instance
            }
        }
    }
}