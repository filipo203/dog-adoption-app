package com.example.dogadoption.room

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
    suspend fun insertAllDogBreeds(dogNames: List<DogNames>)

    @Query("SELECT image_urls FROM dog_pictures WHERE breed_name =:breedName")
    fun getDogBreedImages(breedName: String): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDogBreedImages(dogBreeds: DogImages)
}