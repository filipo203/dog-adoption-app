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
    suspend fun saveDogBreed(dogNames: List<DogNames>) {
        dogDao.saveDogBreeds(dogNames)
    }

    fun getDogBreedImages(breed: String): Flow<List<String>> {
        return dogDao.getDogBreedImages(breed)
    }
    suspend fun saveDogBreedImages(dogImages: DogImages) {
        dogDao.saveDogBreedImages(dogImages)
    }
}