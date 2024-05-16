package com.example.dogadoption.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 2, entities = [DogNames::class, DogImages::class, User::class], exportSchema = false)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
    abstract fun userDao(): UserDao
}
