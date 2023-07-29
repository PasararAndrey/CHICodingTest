package dev.chicodingtest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.chicodingtest.data.PreferenceManager
import dev.chicodingtest.data.UserRepository
import dev.chicodingtest.model.User
import dev.chicodingtest.util.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val vmIOCoroutineContext = viewModelScope.coroutineContext + Dispatchers.IO
    val sortOrder: Flow<SortOrder> = preferenceManager.preferencesFlow
    val users = sortOrder.flatMapLatest { order -> userRepository.getUsers(order) }.asLiveData()

    fun toggleUserStudentStatus(clickedUser: User) {
        viewModelScope.launch(vmIOCoroutineContext) {
            val newUser: User = clickedUser.copy(isStudent = !clickedUser.isStudent)
            Log.d(TAG, "Updated user: $newUser")
            userRepository.updateUserStudentStatus(newUser.id, newUser.isStudent)
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch(vmIOCoroutineContext) {
            userRepository.deleteUser(userId)
        }
    }

    fun updateSortOrder(sortOrder: SortOrder) = viewModelScope.launch(vmIOCoroutineContext) {
        preferenceManager.updateSortOrder(sortOrder)
    }

    companion object {
        private const val TAG = "UserViewModel"
    }

    class Factory(
        private val userRepository: UserRepository,
        private val preferenceManager: PreferenceManager
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(userRepository, preferenceManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

