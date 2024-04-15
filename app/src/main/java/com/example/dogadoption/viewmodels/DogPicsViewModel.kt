package com.example.dogadoption.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogPicsViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _dogPicturesLiveData = MutableLiveData<List<String>>()
    val dogPicturesLiveData: LiveData<List<String>> get() = _dogPicturesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun fetchDogPictures(breed: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val result = dogRepository.getDogPictures(breed)
                if (result.isSuccess) {
                    val images = result.getOrThrow()
                    _dogPicturesLiveData.postValue(images)
                } else {
                    _errorLiveData.postValue("DOG_PICS_VIEW_MODEL: Failed to fetch dog pictures for ${breed}: ${result.exceptionOrNull()?.message ?: "Unknown error"}")
                }
            } catch (exception: Exception) {
                _errorLiveData.postValue("DOG_PICS_VIEW_MODEL: Failed to fetch dog pictures for ${breed}: ${exception.message}")
            }
        }
    }
}
