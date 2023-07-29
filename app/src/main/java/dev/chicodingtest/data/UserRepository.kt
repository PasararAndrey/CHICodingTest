package dev.chicodingtest.data

import dev.chicodingtest.model.User
import dev.chicodingtest.util.SortOrder
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun getUsers(sortOrder: SortOrder): Flow<List<User>> = userDao.getAllUsers(sortOrder)
    suspend fun updateUserStudentStatus(userId: Int, isStudent: Boolean) = userDao.updateIsStudent(userId, isStudent)
    suspend fun getUserById(userId: Int) = userDao.getUserById(userId)
    suspend fun addUser(user: User) = userDao.insertUser(user)
    suspend fun deleteUser(userId: Int) = userDao.deleteUserById(userId)

}