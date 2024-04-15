package com.example.dogadoption.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ListenableWorker
import coil.compose.AsyncImagePainter
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    /*
    private val _dogPicturesLiveData = MutableLiveData<List<String>>()
    val dogPicturesLiveData: LiveData<List<String>> get() = _dogPicturesLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun fetchDogPictures(breed: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val result = dogRepository.getDogPictures(breed)
                if (result is List<*>) {
                    val images = result.filterIsInstance<String>()
                    withContext(Dispatchers.Main) {
                        Log.e(ContentValues.TAG, "Fetched dog pictures for ${breed} [SUCCESS!]")
                        _dogPicturesLiveData.value = images
                    }
                } else {
                    throw IllegalArgumentException("Unexpected result type: $result")
                }
            } catch (exception: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(ContentValues.TAG, "Failed to fetch dog pictures", exception)
                    _errorLiveData.value = "Failed to fetch dog pictures: ${exception.message}"
                }
            }
        }
    } */
/*
    val dogPicturesLiveData = MutableLiveData<List<String>>()
    val errorLiveData = MutableLiveData<String>()

    fun fetchDogPictures(breed: String) {
        viewModelScope.launch(dispatcher) {
            val result = runCatching {
                dogRepository.getDogPictures(breed)
                Log.e(ContentValues.TAG, "Fetching dog pictures for breed: $breed")
            }
            result.onSuccess { images ->
                val typedImages = images as? List<String>
                dogPicturesLiveData.value = typedImages ?: emptyList()
                Log.e(ContentValues.TAG, "Fetched dog pictures for ${breed} [SUCCESS!]")
            }.onFailure { exception ->
                Log.e(ContentValues.TAG, "Failed to fetch dog pictures", exception)
                errorLiveData.value = "Failed to fetch dog pictures: ${exception.message}"
            }
            /*
            val result = runCatching {
            dogRepository.getDogPictures(breed)
        }
            result.onSuccess { result ->
                if (result is List<*>) {
                    val images = result.filterIsInstance<String>()

                    dogPicturesLiveData.value = images
                } else {
                    Log.e(ContentValues.TAG, "Expected result type: $result")
                    errorLiveData.value = "Unexpected result type"
                }
            }.onFailure { exception ->

                errorLiveData.value = "Failed to fetch dog pictures: ${exception.message}"
            }
            */
        }
        /*
        private var isDataFetched = false

    private val _dogPictures = MutableStateFlow<List<String>>(emptyList())
    val dogPictures: StateFlow<List<String>> get() = _dogPictures

    fun fetchDogPictures(breed: String) {
        if (!isDataFetched) {
            viewModelScope.launch {
                try {
                    Log.d(ContentValues.TAG, "Fetching dog pictures for breed: $breed")

                    val pictures = dogRepository.getDogPictures(breed)
                    if (pictures.isNotEmpty()) {

                        _dogPictures.value = pictures
                        isDataFetched = true
                    } else {
                        _dogPictures.value = emptyList()
                        Log.d(ContentValues.TAG, "Dog pictures fetched but empty.")
                    }
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error fetching dog pictures: ${e.message}")
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        isDataFetched = false
    }*/
    }

 */
}
