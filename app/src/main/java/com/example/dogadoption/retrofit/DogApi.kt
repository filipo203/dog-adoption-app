package com.example.dogadoption.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    @GET("breeds/list/all")
    suspend fun getDogBreeds(): DogNames

    @GET("breed/{breed}/images")
    suspend fun getDogPictures(@Path("breed") breed: String): Response<DogPictures>
}