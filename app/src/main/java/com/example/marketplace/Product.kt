package com.example.marketplace

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
//    val firestoreId: String = "",
    val photo: String = "",
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val state: String = "",
    val address: String = "",
    val description: String = "",
    val ownerEmail: String = "",
    val ownerName: String = ""
)
