package com.example.dogadoption.repository

import android.util.Log
import com.example.dogadoption.room.DogNames
import com.example.dogadoption.room.DogImages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource,
) {
    fun getDogBreeds(): Flow<List<DogNames>> {
        return localSource.getDogBreeds()
    }

    fun getDogImages(breed: String): Flow<List<String>> {
        Log.e("DOG_REPOSITORY", "Fetched dog images from database for $breed")
        return localSource.getDogBreedImages(breed)
    }

    suspend fun saveDogBreeds() {
        val dogBreeds = localSource.getDogBreeds().firstOrNull()
        if (dogBreeds.isNullOrEmpty()) {
            try {
                val remoteDogBreeds = remoteSource.getDogBreeds()
                val dogNamesList = remoteDogBreeds.map { DogNames(0, it, "") }
                localSource.saveDogBreed(dogNamesList)
            } catch (e: Exception) {
                Log.e("DOG_REPOSITORY", "Failed to save dog breeds: ${e.message}")
                throw e
            }
        }
    }

    suspend fun saveDogPictures(breed: String) {
        val dogImageDB = localSource.getDogBreedImages(breed).firstOrNull() ?: emptyList()
        if (dogImageDB.isEmpty()) {
            try {
                val result = remoteSource.getDogPictures(breed)
                result.onSuccess { dogPictures ->
                    dogPictures.forEach { imageUrl ->
                        if (!dogImageDB.contains(imageUrl)) {
                            val dogImages = DogImages(0, imageUrl, breed)
                            localSource.saveDogBreedImages(dogImages)
                        }
                    }
                    Log.d("DOG_REPOSITORY", "Dog pictures saved to database for $breed")
                }
            } catch (e: Exception) {
                Log.e("DOG_REPOSITORY", "Failed to save dog pictures to database: ${e.message}")
                throw e
            }
        }
    }
}
