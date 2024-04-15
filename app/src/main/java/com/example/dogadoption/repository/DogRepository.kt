package com.example.dogadoption.repository

import android.content.ContentValues
import android.util.Log
import com.example.dogadoption.retrofit.DogApi
import com.example.dogadoption.retrofit.DogNames
import com.example.dogadoption.retrofit.DogPictures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DogRepository @Inject constructor(
    private val dogApi: DogApi
) {
    suspend fun getDogBreeds(): List<String> {
        return withContext(Dispatchers.IO) {
            dogApi.getDogBreeds().message.keys.toList()
        }
    }

    suspend fun getDogPictures(breed: String): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val response = dogApi.getDogPictures(breed)
                handleApiResponse(response)
            }
        }
    }
    private fun handleApiResponse(response: Response<DogPictures>): List<String> {
        if (response.isSuccessful) {
            val dogImages = response.body()
            if (dogImages != null && dogImages.status == "success") {
                Log.e(ContentValues.TAG,"REPOSITORY: API response [SUCCESS!]: ${response.code()}")
                return dogImages.message
            } else {
                Log.e(ContentValues.TAG,"REPOSITORY: Unexpected API response: ${response.code()}")
                throw Exception("Unexpected API response: ${response.code()}")
            }
        } else {
            Log.e(ContentValues.TAG,"REPOSITORY: API call failed with code: ${response.code()}")
            throw Exception("API call failed with code: ${response.code()}")
        }
    }

    /*runCatching {
        val response = dogApi.getDogPictures(breed)
        if (response.isFailure) error("Failed to fetch dog pictures: ${response.message()}")

        val body = response.body() ?: error("Missing body")
        body.message.map { it.url }
    }
}

     */
}



/*try {
                val response = dogApi.getDogPictures(breed)
                if (response.isSuccessful) {
                    val images = response.body()?.message ?: emptyList()
                    images.map { it.url }
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                emptyList()
            }
             */

