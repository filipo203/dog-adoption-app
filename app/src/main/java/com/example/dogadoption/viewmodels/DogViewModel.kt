package com.example.dogadoption.viewmodels

import android.content.ContentValues
import android.util.Log
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
class DogViewModel @Inject constructor(
    private val dogRepository: DogRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _dogBreeds = MutableLiveData<List<String>>()
    val dogBreeds: LiveData<List<String>> get() = _dogBreeds

    fun fetchDogBreeds() {
        viewModelScope.launch(dispatcher) {
            val result = runCatching {
                dogRepository.getDogBreeds()
            }
            result.onSuccess { result ->
                val breeds = dogRepository.getDogBreeds()
                _dogBreeds.postValue(breeds)
                Log.e(ContentValues.TAG, "DOG_VIEW_MODEL: List of dog names [SUCCESS!]")
            }
                .onFailure { exception ->
                    Log.e(ContentValues.TAG, "DOG_VIEW_MODEL: Error fetching dog breed names")
                }
        }
    }
}
