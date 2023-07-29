package dev.chicodingtest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    val name: String,
    val age: Int,
    @ColumnInfo(name = "is_student")
    var isStudent: Boolean = false,
    val birthday: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
