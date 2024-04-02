package com.example.dogadoption.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogadoption.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogViewModel @Inject constructor(private val dogRepository: DogRepository) : ViewModel() {
    private val _dogBreeds = MutableLiveData<List<String>>()
    val dogBreeds: LiveData<List<String>> get() = _dogBreeds

    fun fetchDogBreeds() {
        viewModelScope.launch {
            _dogBreeds.value = dogRepository.getDogBreeds()
        }
    }
}
