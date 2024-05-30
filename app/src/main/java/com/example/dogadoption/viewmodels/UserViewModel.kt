package com.example.dogadoption.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.room.dogs.DogImages
import com.example.dogadoption.room.user.User
import com.example.dogadoption.room.user.UserEvent
import com.example.dogadoption.room.user.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _uiState = MutableLiveData(UserState())
    val uiState: LiveData<UserState> get() = _uiState

    private val _favoriteDogs = MutableLiveData<List<DogImages>>(emptyList())
    val favoriteDogs: LiveData<List<DogImages>> get() = _favoriteDogs

    init {
        getUser()
        getFavoriteDogs()
    }
    private fun insertUser(name: String) {
        viewModelScope.launch(dispatcher) {
            val user = User(userName = name)
            dogRepository.insertUser(user)
            _user.postValue(user)
            _uiState.postValue(_uiState.value?.copy(userName = name, isAddingUser = false))
        }
    }

    private fun getUser() {
        viewModelScope.launch(dispatcher) {
            val user = dogRepository.getUser()
            _user.postValue(user)
            user?.let {
                _uiState.postValue(_uiState.value?.copy(userName = it.userName))
            }
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SetUserName -> _uiState.value = _uiState.value?.copy(userName = event.userName)
            UserEvent.SaveUser -> {
                val name = _uiState.value?.userName.orEmpty()
                insertUser(name)
            }
            UserEvent.ShowDialog -> _uiState.value = _uiState.value?.copy(isAddingUser = true)
            UserEvent.HideDialog -> _uiState.value = _uiState.value?.copy(isAddingUser = false)
        }
    }

    private fun getFavoriteDogs() {
        viewModelScope.launch(dispatcher) {
            dogRepository.getFavoriteDogImages().collect { favouriteDogList ->
                _favoriteDogs.postValue(favouriteDogList)
            }
        }
    }
}