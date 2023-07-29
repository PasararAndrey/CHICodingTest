package dev.chicodingtest.database

import dev.chicodingtest.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    fun getUsers(): Flow<List<User>> = userDao.getAllUsers()
    suspend fun updateUserStudentStatus(userId: Int, isStudent: Boolean) = userDao.updateIsStudent(userId, isStudent)
    suspend fun getUserById(userId: Int) = userDao.getUserById(userId)
    suspend fun addUser(user: User) = userDao.insertUser(user)
}