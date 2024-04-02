package com.example.dogadoption.daggerhilt

import android.app.Application
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.retrofit.DogApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDogApi(retrofit: Retrofit): DogApi {
        return retrofit.create(DogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogRepository(dogApi: DogApi, app: Application): DogRepository {
        return DogRepository(dogApi, app)
    }
}

