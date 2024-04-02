package com.example.dogadoption.repository

import android.app.Application
import com.example.dogadoption.retrofit.DogApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val dogApi: DogApi,
    private val appContext: Application
) {
    suspend fun getDogBreeds(): List<String> {
        return dogApi.getDogBreeds().message.keys.toList()
    }

    suspend fun getDogPictures(breed: String): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = dogApi.getDogPictures(breed)
                if (response.isSuccessful) {
                    val images = response.body()?.images ?: emptyList()
                    images.map { it.url }
                } else {
                    throw Exception("Failed to fetch dog pictures: ${response.message()}")
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}