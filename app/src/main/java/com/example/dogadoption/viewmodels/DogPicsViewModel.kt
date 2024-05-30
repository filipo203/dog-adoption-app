package com.example.dogadoption.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.room.dogs.DogImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogPicsViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dogImageData = MutableLiveData<List<DogImages>>(emptyList())
    val dogImageData: LiveData<List<DogImages>> get() = _dogImageData

    private val _favoriteDogs = MutableLiveData<List<DogImages>>(emptyList())
    val favoriteDogs: LiveData<List<DogImages>> get() = _favoriteDogs

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    init {
        getFavoriteDogs()
    }
    private fun setLoading(loading: Boolean) {
        _loading.postValue(loading)
    }
    fun saveDogPictures(breed: String) {
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
        viewModelScope.launch(dispatcher) {
            try {
                val imagesFlow = dogRepository.getDogImages(breed)
                imagesFlow.collect { dogImageUrl ->
                    _dogImageData.postValue(dogImageUrl)
                }
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to fetch dog pictures from database: ${e.message}")
            }
        }
    }

    private fun getFavoriteDogs() {
        viewModelScope.launch(dispatcher) {
            dogRepository.getFavoriteDogImages().collect { favouriteDogList ->
                _favoriteDogs.postValue(favouriteDogList)
            }
        }
    }

    fun toggleFavourite(dogImage: DogImages) {
        viewModelScope.launch(dispatcher) {
            try {
                val updatedDogImage = dogImage.copy(isFavourite = !dogImage.isFavourite)
                dogRepository.toggleFavourite(updatedDogImage)
                _favoriteDogs.postValue(
                    _favoriteDogs.value?.map {
                        if (it.id == updatedDogImage.id) updatedDogImage else it
                    }
                )
                val allImages = (_dogImageData.value ?: emptyList()) + (_favoriteDogs.value ?: emptyList())
                _dogImageData.postValue(allImages)
                Log.d("DOGVIEWMODEL", "Toggled favourite status for image ID: ${dogImage.id}")
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to update favorite status: ${e.message}")
            }
        }
    }
}