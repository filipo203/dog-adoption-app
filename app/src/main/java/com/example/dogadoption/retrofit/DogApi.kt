package com.example.dogadoption.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    @GET("breeds/list/all")
    suspend fun saveDogBreeds(): Response<DogBreeds>

    @GET("breed/{breed}/images")
    suspend fun saveDogPictures(@Path("breed") breed: String): Response<DogPictures>
}