package com.example.cloudbuy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cloudbuy.data.model.CartItem
import com.example.cloudbuy.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    val totalItems: Int get() = _cartItems.value.sumOf { it.quantity }
    val totalPrice: Double get() = _cartItems.value.sumOf { it.subtotal }

    fun addToCart(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val old = current[index]
            current[index] = old.copy(quantity = old.quantity + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _cartItems.value = current
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.value = _cartItems.value.map {
            if (it.product.id == productId) it.copy(quantity = quantity) else it
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun toggleFavorite(productId: String) {
        val set = _favorites.value.toMutableSet()
        if (!set.add(productId)) set.remove(productId)
        _favorites.value = set
    }

    fun isFavorite(productId: String): Boolean = _favorites.value.contains(productId)
}