package com.example.dogadoption.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.room.User
import com.example.dogadoption.room.UserEvent
import com.example.dogadoption.room.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dogBreeds = MutableLiveData<List<String>>()
    val dogBreeds: LiveData<List<String>> get() = _dogBreeds

    private val _dogImages = MutableLiveData<List<String>>(emptyList())
    val dogImages: LiveData<List<String>> get() = _dogImages

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private var dogBreedsFetched = false

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _uiState = MutableLiveData(UserState())
    val uiState: LiveData<UserState> get() = _uiState

    init {
        saveDogBreeds()
        getUser()
    }

    private fun setLoading(loading: Boolean) {
        _loading.postValue(loading)
    }

    private fun saveDogBreeds() {
        //saves dog breeds from remote source to database if empty
        if (!dogBreedsFetched) {
            viewModelScope.launch(dispatcher) {
                try {
                    dogRepository.saveDogBreeds()
                    dogBreedsFetched = true
                    Log.d("DOGVIEWMODEL", "Dog breeds saved to database successfully!")
                } catch (e: Exception) {
                    Log.e("DOGVIEWMODEL", "Failed to save dog breeds to database: ${e.message}")
                }
            }
        }
    }

    fun getDogBreeds() {
        //gets dog breeds from database
        viewModelScope.launch(dispatcher) {
            try {
                val breedsFlow = dogRepository.getDogBreeds()
                breedsFlow.collect { dogNamesList ->
                    val breedNames = dogNamesList.map { it.name }
                    _dogBreeds.postValue(breedNames)
                    Log.d(
                        "DOGVIEWMODEL",
                        "Dog breeds fetched from database successfully: $breedNames"
                    )
                }
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to fetch dog breeds from database: ${e.message}")
            }
        }
    }

    fun searchDogBreeds(query: String) {
        //searches for dog breeds from database
        viewModelScope.launch(dispatcher) {
            setLoading(true)
            try {
                val searchBreeds = dogRepository.searchDogBreeds(query)
                val breedNames = searchBreeds.map { it.name }
                _dogBreeds.postValue(breedNames)
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Error searching for dogs")
            } finally {
                setLoading(false)
            }
        }
    }

    fun saveDogPictures(breed: String) {
        //saves dog images for the selected breed from remote source to database if empty
        viewModelScope.launch(dispatcher) {
            setLoading(true)
            try {
                dogRepository.saveDogPictures(breed)
                Log.d("DOGVIEWMODEL", "Dog pictures saved to database for $breed")
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to save dog pictures to database: ${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    fun getDogImages(breed: String) {
        //gets dog images from database
        viewModelScope.launch(dispatcher) {
            try {
                val imagesFlow = dogRepository.getDogImages(breed)
                imagesFlow.collect { imageUrlsList ->
                    // Update LiveData with new image URLs
                    _dogImages.postValue(imageUrlsList)
                    Log.d(
                        "DOGVIEWMODEL",
                        "Dog pictures fetched from database successfully for $breed : $imagesFlow"
                    )
                }
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to fetch dog pictures from database: ${e.message}")
            }
        }
    }
    fun insertUser(name: String) {
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
}