package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartViewModel: CartViewModel, onCheckout: () -> Unit) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val priceFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrinho (${cartItems.size})") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CloudBlue, titleContentColor = CloudWhite)
            )
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCart, null, Modifier.size(72.dp), CloudLightText)
                    Text("Carrinho vazio", color = CloudLightText)
                }
            }
        } else {
            Column(Modifier.fillMaxSize().padding(padding)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems, key = { it.product.id }) { item ->
                        Card(shape = RoundedCornerShape(12.dp)) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = item.product.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(item.product.name, fontWeight = FontWeight.SemiBold)
                                    Text(priceFormat.format(item.product.price), color = CloudBlueDark, fontWeight = FontWeight.Bold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = {
                                            cartViewModel.updateQuantity(item.product.id, item.quantity - 1)
                                        }) { Icon(Icons.Default.Remove, null) }
                                        Text("${item.quantity}", fontWeight = FontWeight.Bold)
                                        IconButton(onClick = {
                                            cartViewModel.updateQuantity(item.product.id, item.quantity + 1)
                                        }) { Icon(Icons.Default.Add, null) }
                                    }
                                }
                                IconButton(onClick = { cartViewModel.removeFromCart(item.product.id) }) {
                                    Icon(Icons.Default.Delete, null, tint = CloudRed)
                                }
                            }
                        }
                    }
                }
                Surface(tonalElevation = 4.dp) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(priceFormat.format(cartViewModel.totalPrice), fontWeight = FontWeight.Bold, color = CloudBlueDark, fontSize = 18.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = onCheckout,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CloudBlue)
                        ) {
                            Text("Finalizar Compra", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}