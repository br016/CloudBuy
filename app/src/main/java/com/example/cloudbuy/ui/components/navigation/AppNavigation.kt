package com.example.cloudbuy.ui.components.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.cloudbuy.ui.components.CloudBuyBottomNav
import com.example.cloudbuy.ui.components.screens.*
import com.example.cloudbuy.viewmodel.AuthViewModel
import com.example.cloudbuy.viewmodel.CartViewModel
import com.example.cloudbuy.viewmodel.ProductViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Para onde voltar depois do login (home ou checkout)
    var afterLoginRoute by remember { mutableStateOf("home") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf("home", "search", "cart", "favorites", "profile")

    fun goAfterAuth() {
        val dest = afterLoginRoute
        afterLoginRoute = "home"
        navController.navigate(dest) {
            // limpa login/register da pilha
            popUpTo("login") { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CloudBuyBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            // COMEÇA NO LOGIN
            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                composable("login") {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onLoginSuccess = { goAfterAuth() },
                        onNavigateToRegister = {
                            navController.navigate("register")
                        },
                        onContinueAsGuest = {
                            authViewModel.continueAsGuest()
                            goAfterAuth()
                        }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        authViewModel = authViewModel,
                        onRegisterSuccess = { goAfterAuth() },
                        onBack = {
                            authViewModel.clearError()
                            // se veio do fluxo de pagamento, volta; senão fica no login
                            if (!navController.popBackStack()) {
                                navController.navigate("login") {
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }

                composable("home") {
                    HomeScreen(
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel,
                        onProductClick = { navController.navigate("product/$it") },
                        onCategoryClick = { navController.navigate("category/$it") },
                        onSearchClick = {
                            navController.navigate("search") {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }

                composable("search") {
                    SearchScreen(
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel,
                        onProductClick = { navController.navigate("product/$it") }
                    )
                }

                composable("cart") {
                    CartScreen(
                        cartViewModel = cartViewModel,
                        onCheckout = {
                            if (isLoggedIn) {
                                // já tem conta → paga direto
                                navController.navigate("checkout")
                            } else {
                                // sem conta → login/cadastro para continuar o pagamento
                                afterLoginRoute = "checkout"
                                navController.navigate("login")
                            }
                        }
                    )
                }

                composable("favorites") {
                    FavoritesScreen(
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel,
                        onProductClick = { navController.navigate("product/$it") }
                    )
                }

                composable("profile") {
                    ProfileScreen(
                        authViewModel = authViewModel,
                        onMyOrders = { navController.navigate("orders") },
                        onLoginClick = {
                            afterLoginRoute = "profile"
                            navController.navigate("login")
                        },
                        onLogout = {
                            authViewModel.logout()
                            afterLoginRoute = "home"
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(
                    "product/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("productId") ?: return@composable
                    val products by productViewModel.products.collectAsState()
                    val product = products.find { it.id == id } ?: return@composable
                    ProductDetailScreen(
                        product = product,
                        cartViewModel = cartViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(
                    "category/{categoryName}",
                    arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
                ) { entry ->
                    val name = entry.arguments?.getString("categoryName") ?: ""
                    CategoryScreen(
                        categoryName = name,
                        productViewModel = productViewModel,
                        cartViewModel = cartViewModel,
                        onBack = { navController.popBackStack() },
                        onProductClick = { navController.navigate("product/$it") }
                    )
                }

                composable("checkout") {
                    CheckoutScreen(
                        cartViewModel = cartViewModel,
                        onBack = { navController.popBackStack() },
                        onFinish = {
                            cartViewModel.clearCart()
                            navController.navigate("home") {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable("orders") {
                    OrdersScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}