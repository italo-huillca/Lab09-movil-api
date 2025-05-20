package com.huillca.lab09_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.huillca.lab09_api.ui.product.ProductViewModel
import com.huillca.lab09_api.ui.product.ScreenProduct
import com.huillca.lab09_api.ui.product.ScreenProducts
import com.huillca.lab09_api.ui.theme.Lab09apiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab09apiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: ProductViewModel = viewModel()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "products"
                    ) {
                        composable("products") {
                            ScreenProducts(
                                viewModel = viewModel,
                                onProductClick = { productId ->
                                    navController.navigate("product/$productId")
                                }
                            )
                        }
                        
                        composable(
                            route = "product/{productId}",
                            arguments = listOf(
                                navArgument("productId") {
                                    type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments?.getInt("productId") ?: 1
                            ScreenProduct(
                                viewModel = viewModel,
                                productId = productId,
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}