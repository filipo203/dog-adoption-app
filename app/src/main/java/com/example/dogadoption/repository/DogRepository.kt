package com.example.dogadoption.repository

import android.util.Log
import com.example.dogadoption.room.dogs.DogNames
import com.example.dogadoption.room.dogs.DogImages
import com.example.dogadoption.room.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val remoteSource: RemoteSource,
    private val localSource: LocalSource
) {
    fun getDogBreeds(): Flow<List<DogNames>> {
        return localSource.getDogBreeds()
    }

    fun getDogImages(breed: String): Flow<List<DogImages>> {
        Log.e("DOG_REPOSITORY", "Fetched dog images from database for $breed")
        return localSource.getDogBreedImages(breed)
    }

    suspend fun saveDogBreeds() {
        val dogBreeds = localSource.getDogBreeds().firstOrNull()
        if (dogBreeds.isNullOrEmpty()) {
            try {
                val remoteDogBreeds = remoteSource.saveDogBreeds()
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
                val result = remoteSource.saveDogPictures(breed)
                result.onSuccess { dogPictures ->
                    dogPictures.forEach { imageUrl ->
                        if (!dogImageDB.any { it.imageUrls == imageUrl}) {
                            val dogImages = DogImages(0, imageUrl, breed, false )
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
    fun searchDogBreeds(query: String): List<DogNames> {
        return localSource.searchDogBreeds(query)
    }
    suspend fun toggleFavourite(dogImage: DogImages) {
        return localSource.toggleFavourite(dogImage)
    }
    fun getFavoriteDogImages(): Flow<List<DogImages>> {
        return localSource.getFavoriteDogImages()
    }
    suspend fun insertUser(user: User) {
        return localSource.insertUser(user)
    }
    fun getUser(): Flow<User?> {
        return localSource.getUser()
    }
}
