package com.huillca.lab09_api.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huillca.lab09_api.data.model.ProductModel
import com.huillca.lab09_api.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    private var currentPage = 0
    private var hasMoreProducts = true
    
    private val _productsState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val productsState: StateFlow<ProductsUiState> = _productsState.asStateFlow()
    
    private val _selectedProductState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val selectedProductState: StateFlow<ProductUiState> = _selectedProductState.asStateFlow()
    
    init {
        loadProducts()
    }
    
    fun loadProducts() {
        if (!hasMoreProducts) return
        
        viewModelScope.launch {
            try {
                if (currentPage == 0) {
                    _productsState.value = ProductsUiState.Loading
                }
                
                Log.d("ProductViewModel", "Cargando página $currentPage")
                val response = repository.getProducts(limit = 10, skip = currentPage * 10)
                
                _productsState.update { currentState ->
                    when (currentState) {
                        is ProductsUiState.Success -> {
                            val currentProducts = currentState.products.toMutableList()
                            currentProducts.addAll(response.products)
                            ProductsUiState.Success(currentProducts)
                        }
                        else -> ProductsUiState.Success(response.products)
                    }
                }
                
                currentPage++
                hasMoreProducts = response.products.isNotEmpty() && 
                    (currentPage * 10) < response.total
                
                Log.d("ProductViewModel", "Productos cargados: ${response.products.size}")
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error al cargar los productos", e)
                _productsState.value = ProductsUiState.Error(
                    e.message ?: "Error desconocido al cargar los productos"
                )
            }
        }
    }
    
    fun loadProduct(id: Int) {
        viewModelScope.launch {
            try {
                Log.d("ProductViewModel", "Iniciando carga del producto $id")
                _selectedProductState.value = ProductUiState.Loading
                val product = repository.getProduct(id)
                Log.d("ProductViewModel", "Producto cargado exitosamente: ${product.title}")
                _selectedProductState.value = ProductUiState.Success(product)
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error al cargar el producto", e)
                _selectedProductState.value = ProductUiState.Error(
                    e.message ?: "Error desconocido al cargar el producto"
                )
            }
        }
    }
}

sealed class ProductsUiState {
    data object Loading : ProductsUiState()
    data class Success(val products: List<ProductModel>) : ProductsUiState()
    data class Error(val message: String) : ProductsUiState()
}

sealed class ProductUiState {
    data object Loading : ProductUiState()
    data class Success(val product: ProductModel) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
} 