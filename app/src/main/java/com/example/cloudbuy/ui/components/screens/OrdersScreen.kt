package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.viewmodel.CartViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    cartViewModel: CartViewModel,
    onNavigateHome: () -> Unit = {}
) {
    val deliveryAddress by cartViewModel.deliveryAddress.collectAsState()

    val calendar = Calendar.getInstance()
    val orderDateFormatted = SimpleDateFormat("dd 'de' MMM, HH:mm", Locale("pt", "BR")).format(calendar.time)

    val deliveryCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 3) }
    val deliveryDateFormatted = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale("pt", "BR")).format(deliveryCalendar.time)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Acompanhar Pedido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateHome) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Chegará até:", fontSize = 13.sp, color = Color.DarkGray)
                            Text(
                                deliveryDateFormatted.replaceFirstChar { it.uppercase() },
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Código do Pedido: #CB-${(100000..999999).random()}", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Status do Envio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    TrackingStep(
                        title = "Pedido Confirmado",
                        subtitle = "Realizado em $orderDateFormatted",
                        isCompleted = true,
                        isCurrent = false,
                        isLast = false
                    )
                    TrackingStep(
                        title = "Em Preparação",
                        subtitle = "O vendedor está embalando seu pacote",
                        isCompleted = true,
                        isCurrent = true,
                        isLast = false
                    )
                    TrackingStep(
                        title = "A caminho da sua região",
                        subtitle = "Transportadora CloudBuy Express",
                        isCompleted = false,
                        isCurrent = false,
                        isLast = false
                    )
                    TrackingStep(
                        title = "Saiu para entrega",
                        subtitle = "O entregador estará a caminho da sua residência",
                        isCompleted = false,
                        isCurrent = false,
                        isLast = true
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Endereço de Entrega", fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = deliveryAddress,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }

            OutlinedButton(
                onClick = onNavigateHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continuar Comprando", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TrackingStep(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> Color(0xFF2E7D32)
                            isCurrent -> MaterialTheme.colorScheme.primary
                            else -> Color.LightGray
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(45.dp)
                        .background(if (isCompleted) Color(0xFF2E7D32) else Color.LightGray)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                title,
                fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Normal,
                fontSize = 15.sp,
                color = if (isCurrent || isCompleted) Color.Unspecified else Color.Gray
            )
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}