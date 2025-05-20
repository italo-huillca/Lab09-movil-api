package com.huillca.lab09_api.data.repository

import com.huillca.lab09_api.data.model.ProductModel
import com.huillca.lab09_api.data.model.ProductsResponse
import com.huillca.lab09_api.data.network.RetrofitClient

class ProductRepository {
    private val apiService = RetrofitClient.productApiService
    
    suspend fun getProducts(limit: Int = 10, skip: Int = 0): ProductsResponse {
        return apiService.getProducts(limit, skip)
    }
    
    suspend fun getProduct(id: Int): ProductModel {
        return apiService.getProduct(id)
    }
} 