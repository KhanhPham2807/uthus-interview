package com.example.uthus.model

import androidx.room.Ignore
import com.example.uthus.model.BeerResponse.Companion.SaveStatus.UNSAVED
import com.google.gson.annotations.SerializedName

data class BeerResponse(
    @SerializedName("price") var price: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("rating") var rating: Rating? = Rating(),
    @SerializedName("image") var image: String? = null,
    @SerializedName("id") var id: Int = -1,
    @SerializedName("sale_off_time") var saleOffTime: Long? = null,
    @Transient var saveStatus: Int =UNSAVED,
    @Transient var note: String?=null

) {
    companion object{
        object SaveStatus {
            const val SAVED = 1
            const val UNSAVED = 2
            const val SAVING = 3

        }
    }
}
