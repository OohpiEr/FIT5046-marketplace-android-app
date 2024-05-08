package com.example.marketplace

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 2, exportSchema = false)
abstract class FavProductDatabase : RoomDatabase() {
    abstract fun favProductDAO(): FavProductDAO

    companion object {
        @Volatile
        private var INSTANCE: FavProductDatabase? = null
        fun getDatabase(context: Context): FavProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavProductDatabase::class.java,
                    "fav_product_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}