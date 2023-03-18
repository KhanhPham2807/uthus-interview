package com.example.uthus.db_manager

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.uthus.model.BeerLocal
import dagger.hilt.android.qualifiers.ApplicationContext


@Database(
    entities = [
        BeerLocal::class],
    version = VERSION_1,
    exportSchema = false
)
abstract class UthusDBManager  : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "UthusDB"
        fun createDatabase(@ApplicationContext appContext: Context) =
            Room.databaseBuilder(appContext, UthusDBManager::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(*ALL_MIGRATIONS)
                .build()

        private val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)
    }
    abstract fun createBeerDao(): BeerDao

}


private const val VERSION_1 = 1

private val MIGRATION_1_2 = object : Migration(VERSION_1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
    }
}