package com.example.dogadoption.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogPicsViewModel @Inject constructor(private val dogRepository: DogRepository) : ViewModel() {
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
                        Log.d(ContentValues.TAG, "Dog pictures fetched successfully: $pictures")
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
    }
}
