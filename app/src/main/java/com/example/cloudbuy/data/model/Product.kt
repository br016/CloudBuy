package com.example.cloudbuy.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val originalPrice: Double? = null,
    val imageUrl: String = "",
    val category: String = "",
    val rating: Double = 0.0,
    val stock: Int = 0,
    val isFeatured: Boolean = false
)