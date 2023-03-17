package com.example.uthus.repository

import com.example.uthus.model.Beer
import com.example.uthus.network.BeerService
import com.example.uthus.network.NetworkResult
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class BeerRepository @Inject constructor(val beerService: BeerService, ) : BaseRepository()
{
     suspend fun getListBeer() :  Flow<NetworkResult<Beer>> {
       return safeApiCallWithLoadingDialog {
            beerService.getListBeer(10,1)
        }
    }


}