package dev.chicodingtest.ui.userdetails

import android.util.Log
import androidx.lifecycle.*
import dev.chicodingtest.database.UserRepository
import dev.chicodingtest.model.User
import kotlinx.coroutines.launch

class UserDetailsViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getUserById(userId: Int) {
        viewModelScope.launch {
            val newUser: User = userRepository.getUserById(userId)
            _user.value = newUser
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared called")
    }

    companion object {
        private const val TAG = "UserDetailsViewModel"
    }

    class Factory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java)) {
                return UserDetailsViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}