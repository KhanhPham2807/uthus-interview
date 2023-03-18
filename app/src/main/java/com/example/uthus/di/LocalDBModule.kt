package com.example.uthus.di

import android.content.Context
import com.example.uthus.db_manager.UthusDBManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDBModule {

    @Singleton
    @Provides
    fun provideChocolatDb(@ApplicationContext app: Context) = UthusDBManager.createDatabase(app)

    @Singleton
    @Provides
    fun provideUserDao(db: UthusDBManager) = db.createBeerDao()
}