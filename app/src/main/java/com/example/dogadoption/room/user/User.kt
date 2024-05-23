package com.example.dogadoption.room.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "user_name")val userName: String
)

