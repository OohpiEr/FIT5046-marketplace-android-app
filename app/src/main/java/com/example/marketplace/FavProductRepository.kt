package com.example.marketplace

import android.app.Application
import kotlinx.coroutines.flow.Flow

class FavProductRepository(application: Application) {

    private var favProductDAO: FavProductDAO =
        FavProductDatabase.getDatabase(application).favProductDAO()

    val allSubjects: Flow<List<Product>> = favProductDAO.getAllFavProducts()

    suspend fun insert(product: Product) {
        favProductDAO.insertFavProduct(product)
    }

    suspend fun delete(product: Product) {
        favProductDAO.deleteFavProduct(product)
    }

    suspend fun update(product: Product) {
        favProductDAO.updateFavProduct(product)
    }

}