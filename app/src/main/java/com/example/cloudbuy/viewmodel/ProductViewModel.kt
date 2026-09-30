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

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProductsFromDatabase()
    }

    fun loadProductsFromDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = SupabaseRepository.getProducts()
                if (list.isNotEmpty() && list.any { it.imageUrl.isNotBlank() }) {
                    _products.value = list
                    _featured.value = list.filter { it.isFeatured }
                } else {
                    useMockData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                useMockData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ADICIONA NOVO PRODUTO NA LOJA
    fun addProduct(newProduct: Product) {
        val currentList = _products.value.toMutableList()
        currentList.add(0, newProduct) // Adiciona no topo da lista
        _products.value = currentList
        if (newProduct.isFeatured) {
            _featured.value = listOf(newProduct) + _featured.value
        }
    }

    fun toggleFavorite(productId: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            current.add(productId)
        }
        _favoriteIds.value = current
    }

    fun isFavorite(productId: String): Boolean {
        return _favoriteIds.value.contains(productId)
    }

    private fun useMockData() {
        val mock = listOf(
            Product(
                id = "1",
                name = "Fone Bluetooth Noise Cancelling",
                description = "Fone de ouvido sem fio premium com cancelamento ativo de ruído, som Hi-Fi e bateria de até 30 horas.",
                price = 299.90,
                originalPrice = 499.90,
                imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600",
                category = "Eletrônicos",
                rating = 4.8,
                stock = 50,
                isFeatured = true
            ),
            Product(
                id = "2",
                name = "Smartwatch Sport GPS Pro",
                description = "Relógio inteligente à prova d'água com monitor cardíaco, contador de passos, GPS e tela AMOLED.",
                price = 599.90,
                originalPrice = 899.90,
                imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600",
                category = "Eletrônicos",
                rating = 4.7,
                stock = 30,
                isFeatured = true
            ),
            Product(
                id = "3",
                name = "Teclado Mecânico RGB Gamer",
                description = "Teclado gamer com switches azuis táteis, iluminação RGB personalizável e estrutura em alumínio.",
                price = 389.90,
                originalPrice = 499.90,
                imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600",
                category = "Eletrônicos",
                rating = 4.9,
                stock = 15,
                isFeatured = true
            ),
            Product(
                id = "6",
                name = "Tênis Esportivo Runner Pro",
                description = "Tênis ultraleve para corrida e caminhada com amortecimento em gel e tecido respirável.",
                price = 349.90,
                originalPrice = 449.90,
                imageUrl = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600",
                category = "Esportes",
                rating = 4.9,
                stock = 80,
                isFeatured = true
            ),
            Product(
                id = "12",
                name = "Mochila Urban Impermeável USB",
                description = "Mochila para notebook de até 15.6 polegadas com saída USB externa e compartimentos organizadores.",
                price = 189.90,
                originalPrice = 249.90,
                imageUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600",
                category = "Acessórios",
                rating = 4.6,
                stock = 45,
                isFeatured = true
            )
        )
        _products.value = mock
        _featured.value = mock.filter { it.isFeatured }
    }
}