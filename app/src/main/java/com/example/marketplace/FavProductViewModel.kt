package com.example.marketplace

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavProductViewModel(application: Application) : AndroidViewModel(application) {
    private val cRepository: FavProductRepository

    init {
        cRepository = FavProductRepository(application)
    }

    val allFavProducts: LiveData<List<Product>> = cRepository.allSubjects.asLiveData()
    fun insertFavProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.insert(product)
    }

    fun updateFavProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.update(product)
    }

    fun deleteFavProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        cRepository.delete(product)
    }

}