package com.example.uthus.repository

import com.example.uthus.model.BeerResponse
import com.example.uthus.network.BeerService
import com.example.uthus.network.NetworkResult
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class BeerRepository @Inject constructor(val beerService: BeerService) : BaseRepository() {
    suspend fun getListBeer(page: Int,shouldShowLoadingDialog :Boolean): Flow<NetworkResult<BeerResponse>> {
        return safeApiCallWithLoadingDialog(shouldShowLoadingDialog) {
            beerService.getListBeer(page = page)
        }
    }


}