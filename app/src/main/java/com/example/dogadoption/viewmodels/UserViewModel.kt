package com.example.dogadoption.viewmodels

import android.util.Log
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserState())
    val uiState: StateFlow<UserState> get() = _uiState

    val favoriteDogs: StateFlow<List<DogImages>> = dogRepository.getFavoriteDogImages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val user: StateFlow<User?> = dogRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = null)
    init {
        user
    }
    private fun insertUser(name: String) {
        viewModelScope.launch(dispatcher) {
            val user = User(userName = name)
            dogRepository.insertUser(user)
            _uiState.value = _uiState.value.copy(userName = name, isAddingUser = false)
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SetUserName -> _uiState.value = _uiState.value.copy(userName = event.userName)
            UserEvent.SaveUser -> {
                val name = _uiState.value.userName
                insertUser(name)
            }
            UserEvent.ShowDialog -> _uiState.value = _uiState.value.copy(isAddingUser = true)
            UserEvent.HideDialog -> _uiState.value = _uiState.value.copy(isAddingUser = false)
        }
    }
}