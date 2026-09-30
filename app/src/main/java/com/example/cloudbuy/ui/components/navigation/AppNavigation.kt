package com.example.cloudbuy.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cloudbuy.data.model.Product
import com.example.cloudbuy.ui.components.screens.*
import com.example.cloudbuy.viewmodel.AuthViewModel
import com.example.cloudbuy.viewmodel.CartViewModel
import com.example.cloudbuy.viewmodel.ProductViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(
                onNavigateToLogin = { navController.navigate("login") { popUpTo("splash") { inclusive = true } } },
                onNavigateToHome = { navController.navigate("home") { popUpTo("splash") { inclusive = true } } }
            )
        }

        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onNavigateToRegister = { navController.navigate("register") },
                onContinueAsGuest = { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                onBack = { navController.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { productId -> navController.navigate("product_detail/$productId") },
                onSearchClick = { navController.navigate("search") },
                onNavigateToFavorites = { navController.navigate("favorites") { popUpTo("home") } },
                onNavigateToCart = { navController.navigate("cart") { popUpTo("home") } },
                onNavigateToProfile = { navController.navigate("profile") { popUpTo("home") } }
            )
        }

        composable("search") {
            SearchScreen(
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { productId -> navController.navigate("product_detail/$productId") },
                onBack = { navController.popBackStack() }
            )
        }

        composable("favorites") {
            FavoritesScreen(
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { productId -> navController.navigate("product_detail/$productId") },
                onNavigateToHome = { navController.navigate("home") { popUpTo(0) } },
                onNavigateToCart = { navController.navigate("cart") { popUpTo("home") } },
                onNavigateToProfile = { navController.navigate("profile") { popUpTo("home") } }
            )
        }

        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                onCheckout = { navController.navigate("checkout") },
                onNavigateToHome = { navController.navigate("home") { popUpTo(0) } },
                onNavigateToFavorites = { navController.navigate("favorites") { popUpTo("home") } },
                onNavigateToProfile = { navController.navigate("profile") { popUpTo("home") } }
            )
        }

        composable("profile") {
            ProfileScreen(
                authViewModel = authViewModel,
                cartViewModel = cartViewModel,
                onMyOrders = { navController.navigate("orders") },
                onNavigateToAddresses = { navController.navigate("addresses") },
                onNavigateToAddProduct = { navController.navigate("add_product") },
                onLoginClick = { navController.navigate("login") },
                onLogout = { navController.navigate("login") { popUpTo(0) } },
                onNavigateToHome = { navController.navigate("home") { popUpTo(0) } },
                onNavigateToFavorites = { navController.navigate("favorites") { popUpTo("home") } },
                onNavigateToCart = { navController.navigate("cart") { popUpTo("home") } }
            )
        }

        composable("add_product") {
            AddProductScreen(
                productViewModel = productViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("addresses") {
            AddressScreen(
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val products by productViewModel.products.collectAsState()
            val product = products.find { it.id == productId } ?: Product(id = productId, name = "Produto")

            ProductDetailScreen(
                product = product,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("checkout") {
            CheckoutScreen(
                authViewModel = authViewModel,
                cartViewModel = cartViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLogin = { navController.navigate("login") },
                onOrderConfirmed = {
                    navController.navigate("orders") { popUpTo("home") { inclusive = false } }
                }
            )
        }

        composable("orders") {
            OrdersScreen(
                cartViewModel = cartViewModel,
                onNavigateHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }
            )
        }
    }
}