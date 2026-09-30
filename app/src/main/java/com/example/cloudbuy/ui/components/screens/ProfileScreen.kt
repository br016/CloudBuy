package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.viewmodel.AuthViewModel
import com.example.cloudbuy.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    onMyOrders: () -> Unit = {},
    onNavigateToAddresses: () -> Unit = {},
    onNavigateToAddProduct: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToCart: () -> Unit = {}
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalCartCount = cartItems.sumOf { it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Perfil", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(selected = false, onClick = onNavigateToHome, icon = { Icon(Icons.Outlined.Home, contentDescription = "Início") }, label = { Text("Início") })
                NavigationBarItem(selected = false, onClick = onNavigateToFavorites, icon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favoritos") }, label = { Text("Favoritos") })
                NavigationBarItem(
                    selected = false, onClick = onNavigateToCart,
                    icon = { BadgedBox(badge = { if (totalCartCount > 0) Badge { Text(totalCartCount.toString()) } }) { Icon(Icons.Outlined.ShoppingBag, contentDescription = "Carrinho") } },
                    label = { Text("Carrinho") }
                )
                NavigationBarItem(
                    selected = true, onClick = { }, icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }, label = { Text("Perfil") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF1565C0), selectedTextColor = Color(0xFF1565C0))
                )
            }
        },
        containerColor = Color(0xFFF5F7FA)
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFFE3F2FD)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF1565C0), modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        if (isLoggedIn) {
                            Text(text = currentUser?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() } ?: "Usuário", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(text = currentUser?.email ?: "", fontSize = 14.sp, color = Color.Gray)
                        } else {
                            Text(text = "Olá, visitante!", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(text = "Faça login para gerenciar sua conta", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Default.AddBusiness,
                        title = "Anunciar / Vender Produto",
                        subtitle = "Cadastrar produto no catálogo da loja",
                        onClick = { if (isLoggedIn) onNavigateToAddProduct() else onLoginClick() }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfileMenuItem(
                        icon = Icons.Default.ReceiptLong,
                        title = "Meus Pedidos",
                        subtitle = "Acompanhar entregas e histórico",
                        onClick = { if (isLoggedIn) onMyOrders() else onLoginClick() }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfileMenuItem(
                        icon = Icons.Default.LocationOn,
                        title = "Endereços",
                        subtitle = "Gerenciar locais de entrega",
                        onClick = { if (isLoggedIn) onNavigateToAddresses() else onLoginClick() }
                    )
                    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF5F5F5))
                    ProfileMenuItem(
                        icon = Icons.Default.HelpOutline,
                        title = "Ajuda e Suporte",
                        subtitle = "Fale conosco",
                        onClick = { }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isLoggedIn) {
                Button(
                    onClick = onLogout, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                    shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFFD32F2F)),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sair da Conta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            } else {
                Button(
                    onClick = onLoginClick, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
                    shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Icon(Icons.Default.Login, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Entrar / Criar Conta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF5F7FA)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = Color(0xFF1565C0), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
            Text(subtitle, fontSize = 13.sp, color = Color.Gray)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}