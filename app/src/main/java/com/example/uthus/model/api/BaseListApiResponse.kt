package com.example.uthus.model.api

import com.google.gson.annotations.SerializedName

class BaseListApiResponse <T> {
    @SerializedName("code")
    var code: Int? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("data")
    var data:  List<T>? = null

    @SerializedName("loadMore")
    var loadMore: Boolean? = null
}