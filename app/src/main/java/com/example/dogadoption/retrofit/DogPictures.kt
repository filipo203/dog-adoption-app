package com.example.dogadoption.retrofit

data class DogPictures(
    val status: String,
    val images: List<DogPicture>
)

data class DogPicture(
    val url: String
)