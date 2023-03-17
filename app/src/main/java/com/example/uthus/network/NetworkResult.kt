package com.example.uthus.network

import com.example.uthus.model.api.BaseErrorApiResponse
import com.example.uthus.model.api.BaseListApiResponse

sealed class NetworkResult<T>(
    val dataResponse: BaseListApiResponse<T>? = null,
    val baseErrorApiResponse: BaseErrorApiResponse? = null,
) {

    class Success<T>(response: BaseListApiResponse<T>?) : NetworkResult<T>(response)


    class RefreshTokenSuccess<T>(response: BaseListApiResponse<T>?) : NetworkResult<T>(response)

    class Error<T>(baseErrorApiResponse: BaseErrorApiResponse) :
        NetworkResult<T>(null, baseErrorApiResponse = baseErrorApiResponse)

    class TokenExpire<T>(baseErrorApiResponse: BaseErrorApiResponse) :
        NetworkResult<T>(null, baseErrorApiResponse = baseErrorApiResponse)

    class LoadingDialog<T> : NetworkResult<T>()

    class NoInternetConnection<T> : NetworkResult<T>()

    class Empty<T> : NetworkResult<T>()

}