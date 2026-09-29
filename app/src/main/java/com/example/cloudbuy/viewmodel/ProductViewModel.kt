package com.example.cloudbuy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudbuy.data.model.Product
import com.example.cloudbuy.data.model.SupabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _featured = MutableStateFlow<List<Product>>(emptyList())
    val featured: StateFlow<List<Product>> = _featured.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProductsFromDatabase()
    }

    fun loadProductsFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Tenta buscar no Supabase real
                val list = SupabaseRepository.getProducts()
                if (list.isNotEmpty()) {
                    _products.value = list
                    _featured.value = list.filter { it.isFeatured }
                } else {
                    useMockData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Se o banco falhar, usa os dados mock de segurança
                useMockData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun useMockData() {
        val mock = listOf(
            Product("1", "Fone Bluetooth Pro", "Fone sem fio com cancelamento de ruído", 299.90, 499.90, "https://picsum.photos/400", "Eletrônicos", 4.5, 120, true),
            Product("2", "Smartwatch Ultra", "Relógio inteligente com GPS", 899.90, 1299.90, "https://picsum.photos/401", "Eletrônicos", 4.8, 45, true),
            Product("3", "Tênis Runner X", "Tênis esportivo ultraleve", 249.90, null, "https://picsum.photos/402", "Esportes", 4.3, 80, true),
            Product("4", "Mochila Urban", "Mochila impermeável 30L", 129.90, 179.90, "https://picsum.photos/403", "Acessórios", 4.6, 60, true)
        )
        _products.value = mock
        _featured.value = mock.filter { it.isFeatured }
    }
}