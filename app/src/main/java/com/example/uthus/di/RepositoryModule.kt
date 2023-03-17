package com.example.uthus.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

//    @Provides
//    fun provideBeerRepository(beerService: BeerService) : BeerRepository {
//        return BeerRepositoryImp(beerService)
//    }
}