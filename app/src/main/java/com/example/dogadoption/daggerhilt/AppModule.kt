package com.example.dogadoption.daggerhilt

import android.content.Context
import androidx.room.Room
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.repository.LocalSource
import com.example.dogadoption.repository.RemoteSource
import com.example.dogadoption.retrofit.DogApi
import com.example.dogadoption.room.DogDao
import com.example.dogadoption.room.DogDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideDogRepository(remoteSource: RemoteSource, localSource: LocalSource): DogRepository {
        return DogRepository(remoteSource, localSource)
    }

    @Provides
    @Singleton
    fun provideDogDatabase(@ApplicationContext context: Context): DogDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            "dog_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDogDao(database: DogDatabase): DogDao {
        return database.dogDao()
    }
}
