package com.example.dogadoption.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey val id: Int = 0,
    val name: String = "",
    val imageUrl: String = ""
)
