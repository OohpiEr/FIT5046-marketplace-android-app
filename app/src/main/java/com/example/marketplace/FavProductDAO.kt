package com.example.marketplace

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavProductDAO {
    @Query("SELECT * FROM Product")
    fun getAllFavProducts(): Flow<List<Product>>

    @Insert
    suspend fun insertFavProduct(product: Product)

    @Update
    suspend fun updateFavProduct(product: Product)

    @Delete
    suspend fun deleteFavProduct(product: Product)
}