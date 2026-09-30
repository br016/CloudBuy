package com.example.cloudbuy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cloudbuy.data.model.CartItem
import com.example.cloudbuy.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddressItem(
    val id: String,
    val title: String,
    val fullAddress: String,
    val street: String = "",
    val number: String = "",
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "",
    val cep: String = "",
    val isDefault: Boolean = false
)

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _purchasedProductIds = MutableStateFlow<Set<String>>(emptySet())
    val purchasedProductIds: StateFlow<Set<String>> = _purchasedProductIds.asStateFlow()

    private val _deliveryAddress = MutableStateFlow("Endereço não informado")
    val deliveryAddress: StateFlow<String> = _deliveryAddress.asStateFlow()

    private val _addresses = MutableStateFlow<List<AddressItem>>(
        listOf(
            AddressItem(
                id = "1",
                title = "Casa",
                fullAddress = "Avenida Paulista, 1000 - Apto 42 - Bela Vista, São Paulo - SP, CEP 01310-100",
                street = "Avenida Paulista",
                number = "1000",
                neighborhood = "Bela Vista",
                city = "São Paulo",
                state = "SP",
                cep = "01310-100",
                isDefault = true
            )
        )
    )
    val addresses: StateFlow<List<AddressItem>> = _addresses.asStateFlow()

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

    fun confirmPurchase(address: String) {
        val newPurchasedIds = _cartItems.value.map { it.product.id }.toSet()
        _purchasedProductIds.value = _purchasedProductIds.value + newPurchasedIds
        _deliveryAddress.value = address
        _cartItems.value = emptyList()
    }

    fun addAddress(address: AddressItem) {
        val current = _addresses.value.toMutableList()
        if (address.isDefault) {
            for (i in current.indices) {
                current[i] = current[i].copy(isDefault = false)
            }
        }
        current.add(address)
        _addresses.value = current
    }

    fun removeAddress(addressId: String) {
        _addresses.value = _addresses.value.filter { it.id != addressId }
    }
}