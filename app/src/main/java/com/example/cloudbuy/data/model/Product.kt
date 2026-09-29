package com.example.cloudbuy.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    @SerialName("original_price")
    val originalPrice: Double? = null,
    @SerialName("image_url")
    val imageUrl: String = "",
    val category: String = "",
    val rating: Double = 0.0,
    val stock: Int = 0,
    @SerialName("is_featured")
    val isFeatured: Boolean = false
)