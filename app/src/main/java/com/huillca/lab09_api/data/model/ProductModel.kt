package com.huillca.lab09_api.data.model

data class ProductModel(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
    val reviews: List<Review>
)

data class Review(
    val rating: Int,
    val comment: String,
    val reviewerName: String,
    val reviewerEmail: String,
    val date: String
) 