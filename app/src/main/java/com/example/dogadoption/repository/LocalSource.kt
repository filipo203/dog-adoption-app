package com.example.dogadoption.repository

import com.example.dogadoption.room.DogDao
import com.example.dogadoption.room.DogImages
import com.example.dogadoption.room.DogNames
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSource @Inject constructor(private val dogDao: DogDao) {

    fun getDogBreeds(): Flow<List<DogNames>> {
        return dogDao.getDogBreeds()
    }
    suspend fun insertDogBreed(dogNames: List<DogNames>) {
        dogDao.insertAllDogBreeds(dogNames)
    }

    fun getDogBreedImages(breed: String): Flow<List<String>> {
        return dogDao.getDogBreedImages(breed)
    }
    suspend fun insertDogBreedImages(dogImages: DogImages) {
        dogDao.insertDogBreedImages(dogImages)
    }
}