package com.example.dogadoption.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.daggerhilt.IoDispatcher
import com.example.dogadoption.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
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

    private var dogBreedsFetched = false
    //private var dogImagesFetched = false

    init {
        fetchAndSaveDogBreeds()
        //fetchDogImages()
    }

    private fun fetchAndSaveDogBreeds() {
        //fetches dog breeds from remote source and saves to database if empty
        if (!dogBreedsFetched) {
            viewModelScope.launch(dispatcher) {
                try {
                    dogRepository.fetchAndSaveDogBreeds()
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
                // Handle error
            }
        }
    }

    fun fetchAndSaveDogPictures(breed: String) {
        // Fetches dog images for the selected breed from remote source and saves to database
        viewModelScope.launch(dispatcher) {
            try {
                dogRepository.fetchAndSaveDogPictures(breed)
                Log.d("DOGVIEWMODEL", "Dog images saved to database for $breed")
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to save dog images to database: ${e.message}")
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
                        "Dog images fetched from database successfully for $breed : $imagesFlow"
                    )
                }
            } catch (e: Exception) {
                Log.e("DOGVIEWMODEL", "Failed to fetch dog images from database: ${e.message}")
                // Handle error
            }
        }
    }
}
