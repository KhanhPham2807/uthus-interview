package com.example.uthus.model.api

import com.google.gson.annotations.SerializedName

open class BaseErrorApiResponse(
    @SerializedName("statusName") var statusName: String? = null,
    @SerializedName("module") var module: String? = null,
    @SerializedName("method") var method: String? = null,
    @SerializedName("message") var message: String? = null


)
