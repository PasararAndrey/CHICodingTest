package dev.chicodingtest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.chicodingtest.model.User

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
                val instance: UserDatabase = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java, "user_database"
                ).build().also { INSTANCE = it }
                instance
            }
        }
    }
}