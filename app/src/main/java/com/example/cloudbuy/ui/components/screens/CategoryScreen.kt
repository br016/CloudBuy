package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cloudbuy.ui.components.ProductCard
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.CartViewModel
import com.example.cloudbuy.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    categoryName: String,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val filtered = products.filter { it.category.equals(categoryName, true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CloudBlue,
                    titleContentColor = CloudWhite,
                    navigationIconContentColor = CloudWhite
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    isFavorite = cartViewModel.isFavorite(product.id),
                    onClick = { onProductClick(product.id) },
                    onFavoriteClick = { cartViewModel.toggleFavorite(product.id) }
                )
            }
        }
    }
}