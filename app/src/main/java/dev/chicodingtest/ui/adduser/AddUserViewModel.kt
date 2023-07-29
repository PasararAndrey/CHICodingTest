package dev.chicodingtest.ui.adduser

import androidx.lifecycle.*
import dev.chicodingtest.database.UserRepository
import dev.chicodingtest.model.User
import dev.chicodingtest.util.Constants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _birthday = MutableLiveData<Long>()
    val birthday: LiveData<Long>
        get() = _birthday
    private val _addUserEvent: Channel<AddUserEvent> = Channel()
    val addUserEvents: Flow<AddUserEvent> = _addUserEvent.receiveAsFlow()
    var userName: String = ""
    var userAge: String = ""

    fun updateBirthday(birthday: Long) {
        _birthday.value = birthday
    }

    fun onAddUser() {
        if (isInputNotValid()) return
        val user = User(userName, userAge.toInt(), birthday = birthday.value!!)
        createUser(user)
    }

    private fun isInputNotValid(): Boolean {
        return (isNameEmpty() || isAgeEmpty() || isAgeNotInt() || isBirthdayNotChosen())
    }

    private fun isBirthdayNotChosen(): Boolean {
        return if (birthday.value == null) {
            showNotValidInput("Birthday must be chosen")
            true
        } else {
            false
        }
    }

    private fun isAgeNotInt(): Boolean {
        return try {
            userAge.toInt()
            false
        } catch (e: NumberFormatException) {
            showNotValidInput("Age must be a number")
            true
        }
    }

    private fun isAgeEmpty(): Boolean {
        return if (userAge.isBlank()) {
            showNotValidInput("Age cannot be empty")
            true
        } else false
    }

    private fun isNameEmpty(): Boolean {
        return if (userName.isBlank()) {
            showNotValidInput("Name cannot be empty")
            true
        } else false
    }

    private fun showNotValidInput(message: String) {
        viewModelScope.launch {
            _addUserEvent.send(AddUserEvent.ShowInvalidInput(message))
        }
    }

    private fun createUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
            _addUserEvent.send(AddUserEvent.NavigateBackWithMessage(Constants.ADD_USER_RESULT_OK))
        }
    }

    sealed class AddUserEvent {
        data class ShowInvalidInput(val message: String) : AddUserEvent()
        data class NavigateBackWithMessage(val result: Int) : AddUserEvent()
    }

    class Factory(
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddUserViewModel::class.java)) {
                return AddUserViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}