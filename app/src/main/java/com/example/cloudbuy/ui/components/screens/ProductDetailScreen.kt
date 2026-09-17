package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cloudbuy.data.model.Product
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    cartViewModel: CartViewModel,
    onBack: () -> Unit
) {
    val priceFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    var qty by remember { mutableIntStateOf(1) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = CloudGreen,
                    contentColor = CloudWhite,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { cartViewModel.toggleFavorite(product.id) }) {
                        Icon(
                            imageVector = if (cartViewModel.isFavorite(product.id))
                                Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoritar",
                            tint = if (cartViewModel.isFavorite(product.id)) CloudRed else CloudDarkText
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 4.dp, shadowElevation = 4.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total", fontSize = 12.sp, color = CloudLightText)
                        Text(
                            priceFormat.format(product.price * qty),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = CloudBlueDark
                        )
                    }

                    Button(
                        onClick = {
                            repeat(qty) { cartViewModel.addToCart(product) }
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (qty == 1)
                                        "✅ ${product.name} adicionado ao carrinho!"
                                    else
                                        "✅ $qty itens adicionados ao carrinho!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CloudBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Adicionar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(product.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                Text(
                    priceFormat.format(product.price),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = CloudBlueDark
                )

                product.originalPrice?.let {
                    Text(
                        priceFormat.format(it),
                        fontSize = 14.sp,
                        color = CloudLightText,
                        style = LocalTextStyle.current.copy(
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text("Quantidade", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (qty > 1) qty-- }) {
                        Icon(Icons.Default.RemoveCircle, contentDescription = null, tint = CloudBlue)
                    }
                    Text(
                        "$qty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = { if (qty < product.stock) qty++ }) {
                        Icon(Icons.Default.AddCircle, contentDescription = null, tint = CloudBlue)
                    }
                    Text(
                        "(${product.stock} disponíveis)",
                        color = CloudLightText,
                        fontSize = 13.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text("Descrição", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text(product.description, color = CloudLightText, lineHeight = 22.sp)
            }
        }
    }
}