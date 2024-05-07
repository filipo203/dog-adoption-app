package com.example.dogadoption.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_pictures")
data class DogImages(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "image_urls") val imageUrls: String,
    @ColumnInfo(name = "breed_name") val breedName: String
)
