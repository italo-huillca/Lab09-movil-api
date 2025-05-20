package com.huillca.lab09_api.ui.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.huillca.lab09_api.data.model.ProductModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProduct(
    viewModel: ProductViewModel,
    productId: Int,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.selectedProductState.collectAsState()
    
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is ProductUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ProductUiState.Success -> {
                val product = (uiState as ProductUiState.Success).product
                ProductDetail(
                    product = product,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is ProductUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as ProductUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetail(
    product: ProductModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = product.thumbnail,
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Marca: ${product.brand}",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Precio: $${product.price}",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Descuento: ${product.discountPercentage}%",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Descripción:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Reseñas:",
            style = MaterialTheme.typography.titleMedium
        )
        
        product.reviews.forEach { review ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = review.reviewerName,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Calificación: ${review.rating}/5",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = review.comment,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
} 