package com.example.cloudbuy.ui.components.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cloudbuy.data.model.Product
import com.example.cloudbuy.viewmodel.AuthViewModel
import com.example.cloudbuy.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    val purchasedIds by cartViewModel.purchasedProductIds.collectAsState()
    val hasPurchased = purchasedIds.contains(product.id)

    var userRating by remember { mutableStateOf(0) }
    var hasRated by remember { mutableStateOf(false) }

    var displayRating by remember { mutableStateOf(product.rating) }
    var ratingCount by remember { mutableStateOf(if (product.rating > 0) 1 else 0) }

    var showReportDialog by remember { mutableStateOf(false) }
    var reportReason by remember { mutableStateOf("") }
    var reportSent by remember { mutableStateOf(false) }

    val reportReasons = listOf(
        "Imagem imprópria (+18)",
        "Conteúdo ofensivo",
        "Produto diferente da foto",
        "Spam / anúncio enganoso",
        "Outro"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { showReportDialog = true }) {
                        Icon(
                            Icons.Outlined.Flag,
                            contentDescription = "Denunciar",
                            tint = Color(0xFFE53935)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.category,
                    fontSize = 13.sp,
                    color = Color(0xFF1565C0),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = currencyFormatter.format(product.price),
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = Color(0xFF1565C0)
                    )
                    product.originalPrice?.let { orig ->
                        if (orig > product.price) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = currencyFormatter.format(orig),
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(16.dp))

                Text("Avaliações do Produto", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (ratingCount > 0) String.format("%.1f", displayRating) else "—",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Color(0xFFFFB300)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = if (index < displayRating.toInt()) Icons.Default.Star
                                    else Icons.Outlined.StarBorder,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = if (ratingCount > 0) "$ratingCount avaliação(ões)" else "Nenhuma avaliação ainda",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasRated || (isLoggedIn && hasPurchased)) Color(0xFFFFF8E1) else Color(0xFFF5F7FA)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        when {
                            !isLoggedIn -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color.Gray)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Avaliações restritas",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Faça login e compre este produto para poder avaliá-lo.",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }

                            !hasPurchased -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Lock, contentDescription = null, tint = Color(0xFFD32F2F))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Compra não verificada",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(0xFFD32F2F)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Apenas clientes que compraram este produto podem deixar uma avaliação.",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                            }

                            else -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF2E7D32))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        if (hasRated) "Sua avaliação enviada" else "Compra Verificada • Avalie o produto",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF2E7D32)
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    repeat(5) { index ->
                                        val starValue = index + 1
                                        Icon(
                                            imageVector = if (starValue <= userRating) Icons.Default.Star
                                            else Icons.Outlined.StarBorder,
                                            contentDescription = "$starValue estrelas",
                                            tint = Color(0xFFFFB300),
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clickable {
                                                    if (!hasRated) {
                                                        userRating = starValue
                                                    }
                                                }
                                                .padding(4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                if (!hasRated) {
                                    Button(
                                        onClick = {
                                            if (userRating == 0) {
                                                Toast.makeText(context, "Selecione de 1 a 5 estrelas", Toast.LENGTH_SHORT).show()
                                            } else {
                                                val total = displayRating * ratingCount + userRating
                                                ratingCount += 1
                                                displayRating = total / ratingCount
                                                hasRated = true
                                                Toast.makeText(context, "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300))
                                    ) {
                                        Text("Enviar Avaliação", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                } else {
                                    Text(
                                        "Obrigado por avaliar com $userRating estrela(s)!",
                                        fontSize = 13.sp,
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(16.dp))

                Text("Descrição", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description.ifBlank { "Sem descrição disponível." },
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Estoque: ${product.stock} un.",
                    fontSize = 13.sp,
                    color = if (product.stock > 0) Color(0xFF2E7D32) else Color.Red
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        cartViewModel.addToCart(product)
                        Toast.makeText(context, "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    enabled = product.stock > 0
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        if (product.stock > 0) "Adicionar ao Carrinho" else "Indisponível",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showReportDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935))
                ) {
                    Icon(Icons.Outlined.Flag, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Denunciar este anúncio", fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            icon = {
                Icon(
                    Icons.Outlined.Flag,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text("Denunciar anúncio", fontWeight = FontWeight.Bold) },
            text = {
                if (reportSent) {
                    Text(
                        "Denúncia enviada com sucesso.\nNossa equipe irá analisar a imagem/conteúdo em breve.",
                        fontSize = 15.sp
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Selecione o motivo da denúncia:",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        reportReasons.forEach { reason ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { reportReason = reason }
                                    .background(
                                        if (reportReason == reason) Color(0xFFFFEBEE)
                                        else Color.Transparent
                                    )
                                    .padding(vertical = 10.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = reportReason == reason,
                                    onClick = { reportReason = reason },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFE53935))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(reason, fontSize = 14.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (reportSent) {
                    Button(
                        onClick = {
                            showReportDialog = false
                            reportSent = false
                            reportReason = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Text("Fechar")
                    }
                } else {
                    Button(
                        onClick = {
                            if (reportReason.isNotBlank()) {
                                reportSent = true
                                Toast.makeText(context, "Denúncia registrada", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        enabled = reportReason.isNotBlank()
                    ) {
                        Text("Enviar denúncia")
                    }
                }
            },
            dismissButton = {
                if (!reportSent) {
                    TextButton(onClick = {
                        showReportDialog = false
                        reportReason = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }
}