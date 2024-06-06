package com.example.dogadoption.repository

import android.util.Log
import com.example.dogadoption.retrofit.DogApi
import com.example.dogadoption.retrofit.DogBreeds
import com.example.dogadoption.retrofit.DogPictures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RemoteSource @Inject constructor(private val dogApi: DogApi) {
    suspend fun saveDogBreeds(): List<String> {
        return withContext(Dispatchers.IO) {
            Log.e("REMOTE_SOURCE", "fun getDogBreeds access")
            val response = dogApi.saveDogBreeds()
            handleApiResponse(response)
        }
    }

    suspend fun saveDogPictures(breed: String): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            Log.e("REMOTE_SOURCE", "fun getDogPictures access")
            val response = dogApi.saveDogPictures(breed)
            handleApiResponsePics(response)
        }
    }

    private fun handleApiResponse(response: Response<DogBreeds>): List<String> {
        if (response.isSuccessful) {
            val dogBreeds = response.body()
            if (dogBreeds != null && dogBreeds.status == "success") {
                Log.e("REMOTE_SOURCE/BREEDS", "API response [PICTURES_SUCCESS!]: ${response.code()}")
                return dogBreeds.message.keys.toList()
            } else {
                Log.e("REMOTE_SOURCE/BREEDS", "Unexpected API response: ${response.code()}")
                throw Exception("Unexpected API response: ${response.code()}")
            }
        } else {
            Log.e("REMOTE_SOURCE/BREEDS", "API call failed with code: ${response.code()}")
            throw Exception("API call failed with code: ${response.code()}")
        }
    }

    private fun handleApiResponsePics(response: Response<DogPictures>): Result<List<String>> {
        return if (response.isSuccessful) {
            val dogImages = response.body()
            if (dogImages != null && dogImages.status == "success") {
                Log.e("REMOTE_SOURCE/PICS", "API response [PICTURES_SUCCESS!]: ${response.code()}")
                Result.success(dogImages.message)
            } else {
                Log.e("REMOTE_SOURCE/PICS", "Unexpected API response: ${response.code()}")
                Result.failure(Exception("Unexpected API response: ${response.code()}"))
            }
        } else {
            Log.e("REMOTE_SOURCE/PICS", "API call failed with code: ${response.code()}")
            Result.failure(Exception("API call failed with code: ${response.code()}"))
        }
    }
}