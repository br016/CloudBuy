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

    private val _purchasedProductIds = MutableStateFlow<Set<String>>(emptySet())
    val purchasedProductIds: StateFlow<Set<String>> = _purchasedProductIds.asStateFlow()

    // Guarda o endereço de entrega do último pedido
    private val _deliveryAddress = MutableStateFlow("Endereço não informado")
    val deliveryAddress: StateFlow<String> = _deliveryAddress.asStateFlow()

    fun addToCart(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(product = product, quantity = 1))
        }
        _cartItems.value = current
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Grava o endereço do pedido e limpa o carrinho
    fun confirmPurchase(address: String) {
        val newPurchasedIds = _cartItems.value.map { it.product.id }.toSet()
        _purchasedProductIds.value = _purchasedProductIds.value + newPurchasedIds
        _deliveryAddress.value = address
        _cartItems.value = emptyList()
    }
}package com.example.cloudbuy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cloudbuy.data.model.CartItem
import com.example.cloudbuy.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _purchasedProductIds = MutableStateFlow<Set<String>>(emptySet())
    val purchasedProductIds: StateFlow<Set<String>> = _purchasedProductIds.asStateFlow()

    // Guarda o endereço de entrega do último pedido
    private val _deliveryAddress = MutableStateFlow("Endereço não informado")
    val deliveryAddress: StateFlow<String> = _deliveryAddress.asStateFlow()

    fun addToCart(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(product = product, quantity = 1))
        }
        _cartItems.value = current
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Grava o endereço do pedido e limpa o carrinho
    fun confirmPurchase(address: String) {
        val newPurchasedIds = _cartItems.value.map { it.product.id }.toSet()
        _purchasedProductIds.value = _purchasedProductIds.value + newPurchasedIds
        _deliveryAddress.value = address
        _cartItems.value = emptyList()
    }
}