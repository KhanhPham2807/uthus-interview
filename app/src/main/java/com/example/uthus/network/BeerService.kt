package com.example.uthus.network

import com.example.uthus.common.Constant.DEFAULT_LIMIT_PAGINATION
import com.example.uthus.model.api.BaseListApiResponse
import com.example.uthus.model.BeerResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface BeerService {
    @GET("sample-data")
    fun getListBeer(
        @Query("limit")limit : Int =DEFAULT_LIMIT_PAGINATION ,
        @Query("page") page : Int =1
    ) : Deferred<BaseListApiResponse<BeerResponse>>
}