package com.example.dogadoption.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [DogNames::class, DogImages::class], exportSchema = false)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
}
