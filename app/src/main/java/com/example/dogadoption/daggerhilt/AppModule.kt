package com.example.dogadoption.daggerhilt

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dogadoption.repository.DogRepository
import com.example.dogadoption.repository.LocalSource
import com.example.dogadoption.repository.RemoteSource
import com.example.dogadoption.retrofit.DogApi
import com.example.dogadoption.room.dogs.DogDao
import com.example.dogadoption.room.dogs.DogDatabase
import com.example.dogadoption.room.user.UserDao
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
        val migration1to2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `user_profile` (" +
                            "`id` INTEGER NOT NULL, " +
                            "`user_name` TEXT NOT NULL, " +
                            "PRIMARY KEY(`id`))"
                )
            }
        }
        val migration2to3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `dog_pictures` ADD COLUMN `is_favourite` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
        return Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            "dog_database"
        ).addMigrations(migration1to2, migration2to3)
        .build()
    }

    @Provides
    @Singleton
    fun provideDogDao(database: DogDatabase): DogDao {
        return database.dogDao()
    }
    @Provides
    @Singleton
    fun provideUserDao(database: DogDatabase): UserDao {
        return database.userDao()
    }

}
