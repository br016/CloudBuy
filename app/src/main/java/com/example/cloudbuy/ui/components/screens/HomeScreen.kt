package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items as lazyListItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.ui.components.ProductCard
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.CartViewModel
import com.example.cloudbuy.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val products by productViewModel.products.collectAsState()
    val featured by productViewModel.featured.collectAsState()
    val categories = listOf("Eletrônicos", "Roupas", "Esportes", "Acessórios")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CloudBuy", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CloudBlue,
                    titleContentColor = CloudWhite
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Busca no topo
            item(span = { GridItemSpan(2) }, key = "search_top") {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onSearchClick() },
                    color = CloudGray,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = CloudLightText)
                        Spacer(Modifier.width(12.dp))
                        Text("Buscar produtos...", color = CloudLightText)
                    }
                }
            }

            item(span = { GridItemSpan(2) }, key = "title_categories") {
                Text("Categorias", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            item(span = { GridItemSpan(2) }, key = "row_categories") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    lazyListItems(categories) { category ->
                        FilterChip(
                            selected = false,
                            onClick = { onCategoryClick(category) },
                            label = { Text(category) }
                        )
                    }
                }
            }

            item(span = { GridItemSpan(2) }, key = "title_featured") {
                Text("Destaques", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(
                items = featured,
                key = { "featured_${it.id}" }
            ) { product ->
                ProductCard(
                    product = product,
                    isFavorite = cartViewModel.isFavorite(product.id),
                    onClick = { onProductClick(product.id) },
                    onFavoriteClick = { cartViewModel.toggleFavorite(product.id) }
                )
            }

            item(span = { GridItemSpan(2) }, key = "title_all") {
                Text("Todos", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(
                items = products,
                key = { "all_${it.id}" }
            ) { product ->
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