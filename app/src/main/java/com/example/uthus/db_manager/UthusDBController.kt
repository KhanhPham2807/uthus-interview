package com.example.uthus.db_manager

import javax.inject.Inject

class UthusDBController  @Inject constructor(val beerDao: BeerDao) {
    suspend fun cleanDBData() {
        try {
            beerDao.deleteAllData()

        } catch (er: Exception) {
            er.printStackTrace()
        }
    }
}