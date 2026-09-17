package com.example.cloudbuy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cloudbuy.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductViewModel : ViewModel() {

    private val mock = listOf(
        Product("1", "Fone Bluetooth Pro", "Fone sem fio com cancelamento de ruído", 299.90, 499.90, "https://picsum.photos/400", "Eletrônicos", 4.5, 120, true),
        Product("2", "Smartwatch Ultra", "Relógio inteligente com GPS", 899.90, 1299.90, "https://picsum.photos/401", "Eletrônicos", 4.8, 45, true),
        Product("3", "Tênis Runner X", "Tênis esportivo ultraleve", 249.90, null, "https://picsum.photos/402", "Esportes", 4.3, 80, true),
        Product("4", "Mochila Urban", "Mochila impermeável 30L", 129.90, 179.90, "https://picsum.photos/403", "Acessórios", 4.6, 60, true),
        Product("5", "Camiseta Dry Fit", "Camiseta para treino", 59.90, null, "https://picsum.photos/404", "Roupas", 4.1, 200, false),
        Product("6", "Carregador Turbo 65W", "Carregador USB-C rápido", 79.90, 99.90, "https://picsum.photos/405", "Eletrônicos", 4.7, 150, false)
    )

    private val _products = MutableStateFlow(mock)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _featured = MutableStateFlow(mock.filter { it.isFeatured })
    val featured: StateFlow<List<Product>> = _featured.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
}