package com.example.dogadoption

import android.app.Application
import androidx.room.Room
import com.example.dogadoption.room.DogDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DogApp: Application() {
/*
    lateinit var database: DogDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            DogDatabase::class.java,
            "dog-database"
        ).build()
    }

 */

}