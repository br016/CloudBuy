package com.example.cloudbuy.ui.components.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cloudbuy.data.model.Product
import com.example.cloudbuy.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    var nameInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var originalPriceInput by remember { mutableStateOf("") }
    var imageUrlInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var stockInput by remember { mutableStateOf("10") }
    var isFeaturedInput by remember { mutableStateOf(false) }

    val categories = listOf("Eletrônicos", "Roupas", "Esportes", "Acessórios")
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anunciar Novo Produto", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
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

            // ─── CARD DE PRÉ-VISUALIZAÇÃO DA FOTO ───
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                if (imageUrlInput.isNotBlank()) {
                    AsyncImage(
                        model = imageUrlInput,
                        contentDescription = "Pré-visualização",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.LightGray
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Cole a URL da imagem abaixo para ver a foto",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // ─── FORMULÁRIO DE CADASTRO ───
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Informações do Produto", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    // NOME
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Nome do Produto *") },
                        placeholder = { Text("Ex: Fone de Ouvido Bluetooth") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = showError && nameInput.isBlank()
                    )

                    // PREÇO E PREÇO ORIGINAL
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = priceInput,
                            onValueChange = { priceInput = it },
                            label = { Text("Preço R$ *") },
                            placeholder = { Text("199.90") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            isError = showError && priceInput.toDoubleOrNull() == null
                        )
                        OutlinedTextField(
                            value = originalPriceInput,
                            onValueChange = { originalPriceInput = it },
                            label = { Text("Preço Original (opcional)") },
                            placeholder = { Text("299.90") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // CATEGORIA
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoria") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        selectedCategory = cat
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // URL DA IMAGEM
                    OutlinedTextField(
                        value = imageUrlInput,
                        onValueChange = { imageUrlInput = it },
                        label = { Text("URL da Imagem (Link da foto) *") },
                        placeholder = { Text("https://exemplo.com/foto.jpg") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = showError && imageUrlInput.isBlank()
                    )

                    // ESTOQUE
                    OutlinedTextField(
                        value = stockInput,
                        onValueChange = { stockInput = it },
                        label = { Text("Quantidade em Estoque") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // DESCRIÇÃO
                    OutlinedTextField(
                        value = descriptionInput,
                        onValueChange = { descriptionInput = it },
                        label = { Text("Descrição Detalhada *") },
                        placeholder = { Text("Informe detalhes, especificações e diferenciais...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        isError = showError && descriptionInput.isBlank()
                    )

                    // DESTAQUE (SWITCH)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Exibir em Destaque", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Aparecerá no topo da página inicial", fontSize = 12.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = isFeaturedInput,
                            onCheckedChange = { isFeaturedInput = it }
                        )
                    }

                    if (showError) {
                        Text(
                            "Preencha todos os campos obrigatórios (*) com valores válidos.",
                            color = Color(0xFFE53935),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // ─── BOTÃO PUBLICAR ───
            Button(
                onClick = {
                    val price = priceInput.toDoubleOrNull()
                    val origPrice = originalPriceInput.toDoubleOrNull()
                    val stock = stockInput.toIntOrNull() ?: 10

                    if (nameInput.isBlank() || price == null || imageUrlInput.isBlank() || descriptionInput.isBlank()) {
                        showError = true
                    } else {
                        val newProd = Product(
                            id = System.currentTimeMillis().toString(),
                            name = nameInput,
                            description = descriptionInput,
                            price = price,
                            originalPrice = origPrice,
                            imageUrl = imageUrlInput,
                            category = selectedCategory,
                            rating = 5.0,
                            stock = stock,
                            isFeatured = isFeaturedInput
                        )

                        productViewModel.addProduct(newProd)
                        Toast.makeText(context, "Produto anunciado com sucesso!", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
            ) {
                Icon(Icons.Default.Publish, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Publicar Produto na Loja", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}