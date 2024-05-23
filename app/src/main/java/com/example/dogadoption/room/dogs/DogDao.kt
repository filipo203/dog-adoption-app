package com.example.dogadoption.room.dogs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DogDao {
    @Query("SELECT * FROM dog_names")
    fun getDogBreeds(): Flow<List<DogNames>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDogBreeds(dogNames: List<DogNames>)

    @Query("SELECT * FROM dog_pictures WHERE breed_name =:breedName")
    fun getDogBreedImages(breedName: String): Flow<List<DogImages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDogBreedImages(dogBreeds: DogImages)

    @Query("SELECT * FROM dog_names WHERE name LIKE :query")
    fun searchDogBreeds(query: String): List<DogNames>

    @Query("UPDATE dog_pictures SET is_favourite = :isFavourite WHERE id = :id")
    suspend fun toggleFavourite(id: Int, isFavourite: Boolean)

    @Query("SELECT * FROM dog_pictures WHERE is_favourite = 1")
    fun getFavoriteDogImages(): Flow<List<DogImages>>
}