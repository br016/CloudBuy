package com.example.cloudbuy.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.cloudbuy.ui.components.theme.CloudBlue

private data class NavItem(val route: String, val icon: ImageVector, val label: String)

@Composable
fun CloudBuyBottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Início"),
        NavItem("cart", Icons.Default.ShoppingCart, "Carrinho"),
        NavItem("favorites", Icons.Default.Favorite, "Favoritos"),
        NavItem("profile", Icons.Default.Person, "Perfil")
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CloudBlue,
                    selectedTextColor = CloudBlue,
                    indicatorColor = CloudBlue.copy(alpha = 0.12f)
                )
            )
        }
    }
}