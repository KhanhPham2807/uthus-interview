package com.example.uthus.repository

import android.util.Log
import com.example.uthus.common.Constant
import com.example.uthus.common.helper.NetworkHelper
import com.example.uthus.db_manager.UthusDBController
import com.example.uthus.model.BeerResponse
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.SAVED
import com.example.uthus.model.api.BaseErrorApiResponse
import com.example.uthus.network.BeerService
import com.example.uthus.network.NetworkResult
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

import javax.inject.Inject

class BeerRepository @Inject constructor(
    val beerService: BeerService,
    val uthusDBController: UthusDBController
) : BaseRepository() {
    suspend fun getListBeer(
        page: Int,
        shouldShowLoadingDialog: Boolean
    ): Flow<NetworkResult<BeerResponse>> {
        return safeApiCallWithLoadingDialog(shouldShowLoadingDialog) {
            beerService.getListBeer(page = page)
        }
    }

    suspend fun getListBeerCheckExist(
        page: Int,
        shouldShowLoadingDialog: Boolean
    ): Flow<NetworkResult<BeerResponse>> {
        return flow<NetworkResult<BeerResponse>> {
            if (NetworkHelper.isNetworkAvailable(context)) {
                if (shouldShowLoadingDialog) {
                    emit(NetworkResult.LoadingDialog())
                }
                val result = beerService.getListBeer(page = page).await()
                result.data?.forEach { beerResponse ->
                    if (uthusDBController.beerDao.exists(beerResponse.id)) {
                        val dataExistedInDB = uthusDBController.beerDao.getBeerByID(beerResponse.id)
                        if (dataExistedInDB.isNotEmpty()) {
                            beerResponse.note = dataExistedInDB.first().note
                            beerResponse.saveStatus = SAVED
                        }
                    }
                }
                emit(NetworkResult.Success(result))

            } else {
                emit(NetworkResult.NoInternetConnection())
            }

        }.flowOn(dispatcher.io)
            .retry(Constant.DEFAULT_RETRY_API_REQUEST_TIMES) {
                true
            }
            .catch { e ->
                Log.d("Henry", e.toString())
                if (e is HttpException) {
                    emit(NetworkResult.Error(handleHTTPErrorResponse(e)))
                } else {
                    emit(NetworkResult.Error(BaseErrorApiResponse(message = "Unknow error")))
                }
            }
    }


}