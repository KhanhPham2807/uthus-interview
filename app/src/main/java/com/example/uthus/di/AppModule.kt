package com.example.uthus.di

import android.content.Context
import com.example.uthus.common.CoroutineDispatcherProvider
import com.example.uthus.repository.BaseRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideCoroutineDispatcher() : CoroutineDispatcherProvider = CoroutineDispatcherProvider()



    @Singleton
    @Provides
    fun provideGson() : Gson = Gson()


}
