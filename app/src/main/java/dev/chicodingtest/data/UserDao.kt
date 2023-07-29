package dev.chicodingtest.data

import androidx.room.*
import dev.chicodingtest.model.User
import dev.chicodingtest.util.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    fun getAllUsers(sortOrder: SortOrder): Flow<List<User>> = when (sortOrder) {
        SortOrder.BY_NAME -> getUsersSortedByName()
        SortOrder.BY_AGE -> getUsersSortedByAge()
        SortOrder.BY_STUDENT_STATUS -> getUsersSortedByStudentStatus()
        SortOrder.BY_DEFAULT -> getUserByDefault()
        SortOrder.BY_DESCRIPTION -> getUsersSortedByDescription()
    }

    @Query("SELECT * FROM users")
    fun getUserByDefault(): Flow<List<User>>

    @Query("SELECT * FROM users ORDER BY name")
    fun getUsersSortedByName(): Flow<List<User>>

    @Query("SELECT * FROM users ORDER BY age")
    fun getUsersSortedByAge(): Flow<List<User>>

    @Query("SELECT * FROM users ORDER BY is_student DESC")
    fun getUsersSortedByStudentStatus(): Flow<List<User>>

    @Query("SELECT * FROM users ORDER BY description")
    fun getUsersSortedByDescription(): Flow<List<User>>

    @Query("UPDATE users SET is_student = :isStudent WHERE id = :id")
    suspend fun updateIsStudent(id: Int, isStudent: Boolean)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User

    @Query("DELETE FROM users WHERE id= :userId")
    suspend fun deleteUserById(userId: Int)
}