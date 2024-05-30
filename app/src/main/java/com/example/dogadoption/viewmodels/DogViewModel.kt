package com.example.dogadoption.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
class DogViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _dogBreeds = MutableLiveData<List<String>>()
    val dogBreeds: LiveData<List<String>> get() = _dogBreeds

    private var dogBreedsFetched = false

    private val _loading = MutableLiveData(false)

    init {
        saveDogBreeds()
    }

    private fun setLoading(loading: Boolean) {
        _loading.postValue(loading)
    }

    private fun saveDogBreeds() {
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
}
