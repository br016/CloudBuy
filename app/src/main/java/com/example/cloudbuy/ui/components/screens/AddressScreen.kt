package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AddressItem(
    val id: String,
    val title: String,
    val fullAddress: String,
    val isDefault: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    onBack: () -> Unit = {}
) {
    // Lista de endereços cadastrados
    var addresses by remember {
        mutableStateOf(
            listOf(
                AddressItem(
                    id = "1",
                    title = "Casa",
                    fullAddress = "Avenida Paulista, 1000 - Apto 42 - Bela Vista, São Paulo - SP, CEP 01310-100",
                    isDefault = true
                )
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }

    // Campos do formulário do novo endereço
    var titleInput by remember { mutableStateOf("") }
    var cepInput by remember { mutableStateOf("") }
    var streetInput by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }
    var neighborhoodInput by remember { mutableStateOf("") }
    var cityInput by remember { mutableStateOf("") }
    var stateInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Endereços", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF1565C0),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Novo Endereço", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFFF5F7FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (addresses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.LightGray
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Nenhum endereço cadastrado",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(addresses) { address ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Home,
                                            contentDescription = null,
                                            tint = Color(0xFF1565C0)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            address.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        if (address.isDefault) {
                                            Spacer(Modifier.width(8.dp))
                                            Surface(
                                                color = Color(0xFFE3F2FD),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    "Principal",
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF1565C0),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            addresses = addresses.filter { it.id != address.id }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Outlined.DeleteOutline,
                                            contentDescription = "Excluir",
                                            tint = Color.Red
                                        )
                                    }
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    address.fullAddress,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ─── DIÁLOG DE ADICIONAR NOVO ENDEREÇO ───
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Novo Endereço", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Identificação (Ex: Casa, Trabalho)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cepInput,
                        onValueChange = { cepInput = it },
                        label = { Text("CEP") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = streetInput,
                        onValueChange = { streetInput = it },
                        label = { Text("Rua / Avenida") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = numberInput,
                            onValueChange = { numberInput = it },
                            label = { Text("Número") },
                            singleLine = true,
                            modifier = Modifier.weight(0.4f)
                        )
                        OutlinedTextField(
                            value = neighborhoodInput,
                            onValueChange = { neighborhoodInput = it },
                            label = { Text("Bairro") },
                            singleLine = true,
                            modifier = Modifier.weight(0.6f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = cityInput,
                            onValueChange = { cityInput = it },
                            label = { Text("Cidade") },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f)
                        )
                        OutlinedTextField(
                            value = stateInput,
                            onValueChange = { stateInput = it.uppercase() },
                            label = { Text("UF") },
                            singleLine = true,
                            modifier = Modifier.weight(0.3f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (streetInput.isNotBlank() && numberInput.isNotBlank()) {
                            val newAddr = AddressItem(
                                id = System.currentTimeMillis().toString(),
                                title = if (titleInput.isBlank()) "Endereço" else titleInput,
                                fullAddress = "$streetInput, $numberInput - $neighborhoodInput, $cityInput - $stateInput, CEP $cepInput",
                                isDefault = addresses.isEmpty()
                            )
                            addresses = addresses + newAddr
                            // Limpa campos
                            titleInput = ""; cepInput = ""; streetInput = ""; numberInput = ""
                            neighborhoodInput = ""; cityInput = ""; stateInput = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}