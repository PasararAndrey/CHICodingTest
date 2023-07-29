package dev.chicodingtest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.chicodingtest.database.UserRepository
import dev.chicodingtest.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val users = userRepository.getUsers().asLiveData()

    fun toggleUserStudentStatus(clickedUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val newUser: User = clickedUser.copy(isStudent = !clickedUser.isStudent)
            Log.d(TAG, "Updated user: $newUser")
            userRepository.updateUserStudentStatus(newUser.id, newUser.isStudent)
        }
    }

    companion object {
        private const val TAG = "UserViewModel"
    }

    class Factory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}