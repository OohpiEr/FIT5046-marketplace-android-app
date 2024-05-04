package com.example.marketplace

import android.app.Application
import kotlinx.coroutines.flow.Flow
class ProductRepository(application: Application) {
    private var productDAO: ProductDAO =
        ProductDatabase.getDatabase(application).productDAO()
    val allSubjects: Flow<List<Product>> = productDAO.getAllSubjects()
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
