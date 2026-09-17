package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onFinish: () -> Unit
) {
    val priceFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    var address by remember { mutableStateOf("") }
    var card by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
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
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Endereço", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Endereço completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Pagamento (simulado)", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = card,
                onValueChange = { card = it },
                label = { Text("Número do cartão") },
                leadingIcon = { Icon(Icons.Default.CreditCard, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = CloudGray)) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total")
                        Text(priceFormat.format(cartViewModel.totalPrice), fontWeight = FontWeight.Bold, color = CloudBlueDark)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CloudGreen)
            ) {
                Text("Confirmar Pedido", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}