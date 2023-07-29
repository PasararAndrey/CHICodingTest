package dev.chicodingtest.ui.adduser

import androidx.lifecycle.*
import dev.chicodingtest.data.UserRepository
import dev.chicodingtest.model.User
import dev.chicodingtest.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddUserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val vmIOCoroutineContext = viewModelScope.coroutineContext + Dispatchers.IO
    private val _birthday = MutableLiveData<Long>()
    val birthday: LiveData<Long>
        get() = _birthday
    private val _addUserEvent: Channel<AddUserEvent> = Channel()
    val addUserEvents: Flow<AddUserEvent> = _addUserEvent.receiveAsFlow()
    val userName = MutableLiveData("")
    val userAge = MutableLiveData("")
    val userDescription = MutableLiveData("")

    fun updateBirthday(birthday: Long) {
        _birthday.value = birthday
    }

    //After validation sure that all values are not null
    fun onAddUser() {
        if (isInputNotValid())
            return
        else {
            val user = User(userName.value!!, userAge.value!!.toInt(), birthday = birthday.value!!, description = userDescription.value!!)
            createUser(user)
        }
    }

    private fun isInputNotValid(): Boolean {
        return (isNameNullOrBlank() || isAgeNullOrBlank() || isDescriptionNullOrBlank() || isAgeNotInt() || isBirthdayNotChosen())
    }

    private fun isDescriptionNullOrBlank(): Boolean {
        return if (userDescription.value.isNullOrBlank()) {
            showNotValidInput("Description cannot be empty")
            true
        } else false
    }

    private fun isBirthdayNotChosen(): Boolean {
        return if (birthday.value == null) {
            showNotValidInput("Birthday must be chosen")
            true
        } else false
    }

    private fun isAgeNotInt(): Boolean {
        return try {
            userAge.value?.toInt() ?: true
            false
        } catch (e: NumberFormatException) {
            showNotValidInput("Age must be a number")
            true
        }
    }

    private fun isAgeNullOrBlank(): Boolean {
        return if (userAge.value.isNullOrBlank()) {
            showNotValidInput("Age cannot be empty")
            true
        } else false
    }

    private fun isNameNullOrBlank(): Boolean {
        return if (userName.value.isNullOrBlank()) {
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
        viewModelScope.launch(vmIOCoroutineContext) {
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