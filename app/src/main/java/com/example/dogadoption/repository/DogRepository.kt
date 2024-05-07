package com.example.dogadoption.repository

import android.content.ContentValues
import android.util.Log
import com.example.dogadoption.retrofit.DogApi
import com.example.dogadoption.retrofit.DogPictures
import com.example.dogadoption.room.DogNames
import com.example.dogadoption.room.DogDao
import com.example.dogadoption.room.DogImages
//import com.example.dogadoption.room.DogLocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) {
    fun getDogBreeds(): Flow<List<DogNames>> {
        return localSource.getDogBreeds()
    }

    fun getDogImages(breed: String): Flow<List<String>> {
        Log.e("DOG_REPOSITORY", "Fetched dog images for $breed")
        return localSource.getDogBreedImages(breed)

    }

    suspend fun fetchAndSaveDogBreeds() {
        val dogBreeds = localSource.getDogBreeds().firstOrNull()
        if (dogBreeds.isNullOrEmpty()) {
            try {
                val remoteDogBreeds = remoteSource.getDogBreeds()
                val dogNamesList = remoteDogBreeds.map { DogNames(0, it, "") }
                localSource.insertDogBreed(dogNamesList)
            } catch (e: Exception) {
                Log.e("DOG_REPOSITORY", "Failed to fetch dog breeds: ${e.message}")
                throw e
            }
        }
    }

    suspend fun fetchAndSaveDogPictures(breed: String) {
        val dogImageDB = localSource.getDogBreedImages(breed).firstOrNull()
        if (dogImageDB.isNullOrEmpty()) {
            try {
                val result = remoteSource.getDogPictures(breed)
                result.onSuccess { dogPictures ->
                    dogPictures.forEach { imageUrl ->
                        val existingImages = localSource.getDogBreedImages(breed).firstOrNull()
                        if (existingImages == null || !existingImages.contains(imageUrl)) {
                            val dogImages = DogImages(0, imageUrl, breed)
                            localSource.insertDogBreedImages(dogImages)
                        }
                    }
                    Log.d("DOG_REPOSITORY", "Dog pictures saved to database for $breed")
                }.onFailure { e ->
                    Log.e("DOG_REPOSITORY", "Failed to fetch dog pictures: ${e.message}")
                    throw e
                }
            } catch (e: Exception) {
                Log.e("DOG_REPOSITORY", "Failed to save dog pictures to database: ${e.message}")
                throw e
            }
        }
    }
}
