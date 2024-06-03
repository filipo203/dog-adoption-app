package com.example.dogadoption.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.room.dogs.DogImages
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
class DogPicsViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dogImageData = MutableStateFlow<List<DogImages>>(emptyList())
    val dogImageData: StateFlow<List<DogImages>> get() = _dogImageData.asStateFlow()

    private val _favoriteDogs = MutableStateFlow<List<DogImages>>(emptyList())
    val favoriteDogs: StateFlow<List<DogImages>> = dogRepository.getFavoriteDogImages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading


    private fun setLoading(loading: Boolean) {
        _loading.value = loading
    }

    fun saveDogPictures(breed: String) {
        viewModelScope.launch(dispatcher) {
            setLoading(true)
            try {
                dogRepository.saveDogPictures(breed)
                Log.d("DOGPICSVIEWMODEL", "saveDogPictures(), Dog pictures saved to database for $breed")
            } catch (e: Exception) {
                Log.e("DOGPICSVIEWMODEL", "saveDogPictures(), Failed to save dog pictures to database: ${e.message}")
            } finally {
                setLoading(false)
            }
        }
    }

    fun getDogImages(breed: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val imagesFlow = dogRepository.getDogImages(breed)
                imagesFlow.collect { dogImageUrl ->
                    _dogImageData.value = dogImageUrl
                }
            } catch (e: Exception) {
                Log.e("DOGPICSVIEWMODEL", "Failed to fetch dog pictures from database: ${e.message}")
            }
        }
    }

    fun toggleFavourite(dogImage: DogImages) {
        viewModelScope.launch(dispatcher) {
            try {
                val updatedDogImage = dogImage.copy(isFavourite = !dogImage.isFavourite)
                dogRepository.toggleFavourite(updatedDogImage)
                _favoriteDogs.value.map {
                    if (it.id == updatedDogImage.id) updatedDogImage else it
                }
                val allImages =
                    (_dogImageData.value) + (_favoriteDogs.value)
                _dogImageData.value = allImages
                Log.d("DOGPICSVIEWMODEL", "Toggled favourite status for image ID: ${dogImage.id}")
            } catch (e: Exception) {
                Log.e("DOGPICSVIEWMODEL", "Failed to update favorite status: ${e.message}")
            }
        }
    }
}