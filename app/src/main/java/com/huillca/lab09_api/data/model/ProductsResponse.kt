package com.huillca.lab09_api.data.model

data class ProductsResponse(
    val products: List<ProductModel>,
    val total: Int,
    val skip: Int,
    val limit: Int
) 