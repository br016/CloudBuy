package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.viewmodel.CartViewModel
import com.example.cloudbuy.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalCartCount = cartItems.sumOf { it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(selected = false, onClick = onNavigateToHome, icon = { Icon(Icons.Outlined.Home, contentDescription = "Início") }, label = { Text("Início") })
                NavigationBarItem(
                    selected = true, onClick = { },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF1565C0), selectedTextColor = Color(0xFF1565C0))
                )
                NavigationBarItem(
                    selected = false, onClick = onNavigateToCart,
                    icon = { BadgedBox(badge = { if (totalCartCount > 0) Badge { Text(totalCartCount.toString()) } }) { Icon(Icons.Outlined.ShoppingBag, contentDescription = "Carrinho") } },
                    label = { Text("Carrinho") }
                )
                NavigationBarItem(selected = false, onClick = onNavigateToProfile, icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") }, label = { Text("Perfil") })
            }
        },
        containerColor = Color(0xFFF5F7FA)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nenhum produto favoritado", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Salve itens para vê-los mais tarde.", fontSize = 14.sp, color = Color.LightGray)
            }
        }
    }
}