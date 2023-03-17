package com.example.blecomunication.model

import com.google.gson.annotations.SerializedName

data class Beer(
    @SerializedName("price"         ) var price       : String? = null,
    @SerializedName("name"          ) var name        : String? = null,
    @SerializedName("rating"        ) var rating      : Rating? = Rating(),
    @SerializedName("image"         ) var image       : String? = null,
    @SerializedName("id"            ) var id          : Int?    = null,
    @SerializedName("sale_off_time" ) var saleOffTime : Int?    = null
)