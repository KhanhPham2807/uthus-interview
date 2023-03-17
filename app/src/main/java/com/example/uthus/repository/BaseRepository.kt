package com.example.uthus.repository

import android.content.Context
import android.util.Log

import com.example.uthus.common.CoroutineDispatcherProvider
import com.example.uthus.common.NetworkHelper
import com.example.uthus.model.api.BaseErrorApiResponse
import com.example.uthus.model.api.BaseListApiResponse
import com.example.uthus.network.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

open class BaseRepository @Inject constructor() {

    @Inject
    lateinit var dispatcher: CoroutineDispatcherProvider

    @Inject
    lateinit var gson: Gson

    @Inject
    @ApplicationContext
    lateinit var context: Context

    suspend fun <T> safeApiCallWithLoadingDialog(
        call: suspend () -> Deferred<BaseListApiResponse<T>>,
    ): Flow<NetworkResult<T>> {
        return flow {
            if (NetworkHelper.isNetworkAvailable(context)) {
                emit(NetworkResult.LoadingDialog())
                val result = call.invoke().await()
                emit(NetworkResult.Success(result))
            } else {
                emit(NetworkResult.NoInternetConnection())
            }

        }.flowOn(dispatcher.io)
            .retry(2L) {
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

    private fun handleHTTPErrorResponse(exception: HttpException): BaseErrorApiResponse {
        return try {
            var errorResponse = BaseErrorApiResponse()
            exception.response()?.errorBody()?.let { it ->
                val errorBodyText = it.charStream().readText()
                errorResponse = gson.fromJson(errorBodyText, BaseErrorApiResponse::class.java)

            }
            errorResponse
        } catch (
            e: Exception
        ) {
            BaseErrorApiResponse(message = "Unknow error")

        }
    }
}