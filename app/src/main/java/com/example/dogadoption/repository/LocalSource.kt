package com.example.dogadoption.repository

import com.example.dogadoption.room.DogDao
import com.example.dogadoption.room.DogImages
import com.example.dogadoption.room.DogNames
import com.example.dogadoption.room.User
import com.example.dogadoption.room.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSource @Inject constructor(
    private val dogDao: DogDao,
    private val userDao: UserDao
) {

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
    fun searchDogBreeds(query: String): List<DogNames> {
        return dogDao.searchDogBreeds("%$query%")
    }
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun getUser(): User? {
        return userDao.getUser()
    }
}