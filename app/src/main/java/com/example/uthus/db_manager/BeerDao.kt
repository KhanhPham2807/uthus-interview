package com.example.uthus.db_manager

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.uthus.model.BeerLocal
import com.example.uthus.model.BeerLocal.Companion.TABLE_NAME

@Dao
interface BeerDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE `id`= :id")
    suspend fun getBeerByID(id: Int): List<BeerLocal>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY ID DESC")
    suspend fun getAllBeer(): List<BeerLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeer(beer: BeerLocal)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAllData()

}