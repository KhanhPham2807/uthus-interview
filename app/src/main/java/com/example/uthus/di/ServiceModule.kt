package com.example.uthus.di

import androidx.annotation.IntDef
import com.example.uthus.network.BeerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {
    @Provides
    @Singleton
    fun provideLoginApiService(retrofit: Retrofit)
            : BeerService = retrofit.create(BeerService::class.java)
}