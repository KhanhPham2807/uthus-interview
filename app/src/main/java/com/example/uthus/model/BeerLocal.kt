package com.example.uthus.model

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.uthus.model.BeerLocal.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME)
data class BeerLocal(
    @ColumnInfo(name = "price") var price: String? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "image") var image: String? = null,
    @ColumnInfo(name = "note") var note: String? = null,
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = null,



    ){
    companion object {
        const val TABLE_NAME = "BeerLocal"
        fun mapData(beerResponse: BeerResponse, note: String?, imageURL : String):BeerLocal{
            val beerLocal = BeerLocal(
                id = beerResponse.id,
                name = beerResponse.name,
                price = beerResponse.price,
                note = note,
                image = imageURL,

                )
            Log.d("Henry", "mapData:  $beerLocal")
            return  beerLocal
        }
    }
}
