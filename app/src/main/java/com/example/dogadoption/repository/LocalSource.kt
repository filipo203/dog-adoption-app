package com.example.dogadoption.repository

import com.example.dogadoption.room.dogs.DogDao
import com.example.dogadoption.room.dogs.DogImages
import com.example.dogadoption.room.dogs.DogNames
import com.example.dogadoption.room.user.User
import com.example.dogadoption.room.user.UserDao
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

    fun getDogBreedImages(breed: String): Flow<List<DogImages>> {
        return dogDao.getDogBreedImages(breed)
    }
    suspend fun saveDogBreedImages(dogImages: DogImages) {
        dogDao.saveDogBreedImages(dogImages)
    }
    fun searchDogBreeds(query: String): List<DogNames> {
        return dogDao.searchDogBreeds("%$query%")
    }
    suspend fun toggleFavourite(dogImage: DogImages) {
        dogDao.toggleFavourite(dogImage.id, dogImage.isFavourite)
    }
    fun getFavoriteDogImages(): Flow<List<DogImages>> {
        return dogDao.getFavoriteDogImages()
    }
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun getUser(): User? {
        return userDao.getUser()
    }
}