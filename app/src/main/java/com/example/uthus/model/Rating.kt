package com.example.uthus.model

import com.google.gson.annotations.SerializedName

data class Rating (

    @SerializedName("average" ) var average : Double? = null,
    @SerializedName("reviews" ) var reviews : Int?    = null

)