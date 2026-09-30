package com.example.cloudbuy.ui.components.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocalAtm
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.viewmodel.AuthViewModel
import com.example.cloudbuy.viewmodel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    authViewModel: AuthViewModel,
    cartViewModel: CartViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onOrderConfirmed: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null

    val cartItems by cartViewModel.cartItems.collectAsState()
    val total = cartItems.sumOf { it.product.price * it.quantity }
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    var cep by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    var selectedPayment by remember { mutableStateOf("pix") }
    var showAddressError by remember { mutableStateOf(false) }
    var isLoadingCep by remember { mutableStateOf(false) }

    var cardNumber by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    // BUSCA REAL VIA CEP
    fun fetchCep(cepToSearch: String) {
        val clean = cepToSearch.replace("-", "").replace(".", "").trim()
        if (clean.length == 8) {
            isLoadingCep = true
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val response = URL("https://viacep.com.br/ws/$clean/json/").readText()
                    val json = JSONObject(response)

                    if (!json.has("erro")) {
                        withContext(Dispatchers.Main) {
                            street = json.optString("logradouro", "")
                            neighborhood = json.optString("bairro", "")
                            city = json.optString("localidade", "")
                            state = json.optString("uf", "")
                            isLoadingCep = false
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            isLoadingCep = false
                            Toast.makeText(context, "CEP não encontrado.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isLoadingCep = false
                        Toast.makeText(context, "Erro de conexão ao buscar CEP.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    if (!isLoggedIn) {
        AlertDialog(
            onDismissRequest = onNavigateBack,
            icon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = { Text("Login necessário", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = { Text("Para finalizar a compra e acompanhar a entrega, você precisa entrar ou criar uma conta.", fontSize = 15.sp) },
            confirmButton = {
                Button(
                    onClick = onNavigateToLogin,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Entrar / Criar Conta") }
            },
            dismissButton = {
                TextButton(onClick = onNavigateBack) { Text("Voltar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF1565C0))
                        Spacer(Modifier.width(8.dp))
                        Text("Entrega e Pagamento", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ENDEREÇO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF1565C0))
                        Spacer(Modifier.width(8.dp))
                        Text("Endereço de Entrega", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("(Obrigatório)", color = Color(0xFFE53935), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = cep,
                            onValueChange = {
                                if (it.length <= 9) {
                                    cep = it
                                    if (it.replace("-", "").length == 8) {
                                        fetchCep(it)
                                    }
                                }
                            },
                            label = { Text("CEP") },
                            placeholder = { Text("Ex: 01001-000") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            isError = showAddressError && cep.isBlank()
                        )
                        Button(
                            onClick = { fetchCep(cep) },
                            modifier = Modifier.height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                            enabled = !isLoadingCep
                        ) {
                            if (isLoadingCep) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("BUSCAR", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Rua / Avenida") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        isError = showAddressError && street.isBlank()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = number,
                            onValueChange = { number = it },
                            label = { Text("Número") },
                            modifier = Modifier.weight(0.4f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            isError = showAddressError && number.isBlank()
                        )
                        OutlinedTextField(
                            value = neighborhood,
                            onValueChange = { neighborhood = it },
                            label = { Text("Bairro") },
                            modifier = Modifier.weight(0.6f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            isError = showAddressError && neighborhood.isBlank()
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("Cidade") },
                            modifier = Modifier.weight(0.7f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            isError = showAddressError && city.isBlank()
                        )
                        OutlinedTextField(
                            value = state,
                            onValueChange = { if (it.length <= 2) state = it.uppercase() },
                            label = { Text("UF") },
                            modifier = Modifier.weight(0.3f),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            isError = showAddressError && state.isBlank()
                        )
                    }

                    if (showAddressError) {
                        Text("Preencha todos os campos do endereço para continuar.", color = Color(0xFFE53935), fontSize = 13.sp)
                    }
                }
            }

            // PAGAMENTO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payments, contentDescription = null, tint = Color(0xFF1565C0))
                        Spacer(Modifier.width(8.dp))
                        Text("Formas de Pagamento", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.width(6.dp))
                        Text("(Simulado)", color = Color.Gray, fontSize = 12.sp)
                    }

                    Spacer(Modifier.height(8.dp))

                    PaymentMethodItem(
                        title = "Pix",
                        subtitle = "Aprovação imediata • 5% de desconto",
                        icon = Icons.Outlined.QrCode2,
                        selected = selectedPayment == "pix",
                        onClick = { selectedPayment = "pix" }
                    )

                    PaymentMethodItem(
                        title = "Cartão de Crédito",
                        subtitle = "Em até 12x sem juros",
                        icon = Icons.Outlined.CreditCard,
                        selected = selectedPayment == "credit_card",
                        onClick = { selectedPayment = "credit_card" }
                    )

                    AnimatedVisibility(visible = selectedPayment == "credit_card") {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(start = 12.dp, top = 8.dp, bottom = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { cardNumber = it },
                                label = { Text("Número do cartão") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp)
                            )
                            OutlinedTextField(
                                value = cardName,
                                onValueChange = { cardName = it },
                                label = { Text("Nome impresso no cartão") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { cardExpiry = it },
                                    label = { Text("Validade") },
                                    placeholder = { Text("MM/AA") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = { cardCvv = it },
                                    label = { Text("CVV") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    PaymentMethodItem(
                        title = "Dinheiro na Entrega",
                        subtitle = "Pague ao receber o pedido",
                        icon = Icons.Outlined.LocalAtm,
                        selected = selectedPayment == "cash",
                        onClick = { selectedPayment = "cash" }
                    )
                }
            }

            // TOTAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total a pagar", color = Color.DarkGray, fontSize = 13.sp)
                        Text(
                            currencyFormatter.format(total),
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Frete", color = Color.Gray, fontSize = 12.sp)
                        Text("GRÁTIS", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 14.sp)
                    }
                }
            }

            // BOTÃO CONFIRMAR
            Button(
                onClick = {
                    if (!isLoggedIn) {
                        onNavigateToLogin()
                        return@Button
                    }
                    val addressOk = cep.isNotBlank() && street.isNotBlank() &&
                            number.isNotBlank() && neighborhood.isNotBlank() &&
                            city.isNotBlank() && state.isNotBlank()

                    if (!addressOk) {
                        showAddressError = true
                    } else {
                        val fullAddress = "$street, $number - $neighborhood, $city - $state, CEP $cep"
                        cartViewModel.confirmPurchase(fullAddress)
                        onOrderConfirmed()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text("CONFIRMAR PEDIDO E PAGAMENTO", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PaymentMethodItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(if (selected) Color(0xFFE3F2FD) else Color.Transparent)
            .border(
                width = if (selected) 1.5.dp else 0.dp,
                color = if (selected) Color(0xFF1565C0) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF1565C0)))
        Spacer(Modifier.width(8.dp))
        Icon(icon, contentDescription = null, tint = if (selected) Color(0xFF1565C0) else Color.Gray, modifier = Modifier.size(26.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = if (selected) Color(0xFF0D47A1) else Color.Unspecified)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}