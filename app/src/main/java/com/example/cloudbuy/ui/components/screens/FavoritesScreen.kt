package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cloudbuy.ui.components.ProductCard
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.CartViewModel
import com.example.cloudbuy.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (String) -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val favorites by cartViewModel.favorites.collectAsState()
    val favProducts = products.filter { favorites.contains(it.id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CloudBlue, titleContentColor = CloudWhite)
            )
        }
    ) { padding ->
        if (favProducts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FavoriteBorder, null, Modifier.size(72.dp), CloudLightText)
                    Text("Nenhum favorito", color = CloudLightText)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        isFavorite = true,
                        onClick = { onProductClick(product.id) },
                        onFavoriteClick = { cartViewModel.toggleFavorite(product.id) }
                    )
                }
            }
        }
    }
}