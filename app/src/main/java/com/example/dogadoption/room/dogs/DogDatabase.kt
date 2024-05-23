package com.example.dogadoption.room.dogs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dogadoption.room.user.User
import com.example.dogadoption.room.user.UserDao

@Database(version = 3, entities = [DogNames::class, DogImages::class, User::class], exportSchema = false)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
    abstract fun userDao(): UserDao
}
