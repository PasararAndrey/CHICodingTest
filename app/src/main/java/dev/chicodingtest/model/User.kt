package dev.chicodingtest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("age")
    val age: Int,
    @ColumnInfo(name = "is_student")
    var isStudent: Boolean = false,
    @ColumnInfo("birthday")
    val birthday: Long,
    @ColumnInfo(name = "description")
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
