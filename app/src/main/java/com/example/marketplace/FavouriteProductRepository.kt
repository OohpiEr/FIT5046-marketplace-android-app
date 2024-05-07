package com.example.marketplace

import android.app.Application
import kotlinx.coroutines.flow.Flow
class FavouriteProductRepository(application: Application) {
    private var productDAO: ProductDAO =
        FavouriteProductDatabase.getDatabase(application).productDAO()
    val allProducts: Flow<List<Product>> = productDAO.getAllProducts()
    suspend fun insert(product: Product) {
        productDAO.insertProduct(product)
    }
    suspend fun delete(product:Product) {
        productDAO.deleteProduct(product)
    }
    suspend fun update(product: Product) {
        productDAO.updateProduct(product)
    }
}
