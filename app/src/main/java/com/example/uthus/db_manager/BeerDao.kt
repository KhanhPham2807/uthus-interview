package com.example.uthus.db_manager

import androidx.room.*
import com.example.uthus.common.Constant.DEFAULT_LIMIT_PAGINATION
import com.example.uthus.model.BeerLocal
import com.example.uthus.model.BeerLocal.Companion.TABLE_NAME


@Dao
interface BeerDao {
    @Query("SELECT * FROM $TABLE_NAME WHERE `id`= :id")
    suspend fun getBeerByID(id: Int): List<BeerLocal>

    @Update
    fun updateUser(user: BeerLocal)

    @Query("SELECT * FROM $TABLE_NAME LIMIT :limit OFFSET :offset")
    fun getBeer(limit: Int = DEFAULT_LIMIT_PAGINATION, offset: Int): List<BeerLocal>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeer(beer: BeerLocal)

    @Query("DELETE FROM $TABLE_NAME")
    fun deleteAllData()

    @Query("DELETE FROM $TABLE_NAME   WHERE `id`= :id")
    fun deleteBeer(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM  $TABLE_NAME WHERE `id` = :id)")
    fun exists(id: Int): Boolean

}