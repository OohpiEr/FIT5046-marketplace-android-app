package com.example.marketplace

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class FavouriteProductDatabase : RoomDatabase() {
    abstract fun productDAO(): ProductDAO

    companion object {
        @Volatile
        private var INSTANCE: FavouriteProductDatabase? = null
        fun getDatabase(context: Context): FavouriteProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteProductDatabase::class.java,
                    "subject_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
