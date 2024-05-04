package com.example.marketplace

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val cRepository: ProductRepository

    init {
        cRepository = ProductRepository(application)
    }

    val allSubjects: LiveData<List<Product>> = cRepository.allSubjects.asLiveData()
    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(product)
    }

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(product)
    }
}

