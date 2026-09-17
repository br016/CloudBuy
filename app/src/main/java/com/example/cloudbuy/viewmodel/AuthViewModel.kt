package com.example.cloudbuy.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUser(
    val name: String = "",
    val email: String = ""
)

class AuthViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val users = mutableMapOf<String, Pair<String, String>>()

    fun clearError() {
        _errorMessage.value = null
    }

    fun login(email: String, password: String): Boolean {
        clearError()
        val e = email.trim().lowercase()
        if (e.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Preencha e-mail e senha"
            return false
        }
        if (e == "admin@cloudbuy.com" && password == "123456") {
            _currentUser.value = AuthUser("Admin", e)
            _isLoggedIn.value = true
            return true
        }
        val user = users[e]
        if (user == null || user.second != password) {
            _errorMessage.value = "E-mail ou senha incorretos"
            return false
        }
        _currentUser.value = AuthUser(user.first, e)
        _isLoggedIn.value = true
        return true
    }

    fun register(name: String, email: String, password: String, confirmPassword: String): Boolean {
        clearError()
        val n = name.trim()
        val e = email.trim().lowercase()
        if (n.isEmpty() || e.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Preencha todos os campos"
            return false
        }
        if (!e.contains("@")) {
            _errorMessage.value = "E-mail inválido"
            return false
        }
        if (password.length < 4) {
            _errorMessage.value = "Senha muito curta"
            return false
        }
        if (password != confirmPassword) {
            _errorMessage.value = "Senhas não coincidem"
            return false
        }
        if (users.containsKey(e)) {
            _errorMessage.value = "E-mail já cadastrado"
            return false
        }
        users[e] = n to password
        _currentUser.value = AuthUser(n, e)
        _isLoggedIn.value = true
        return true
    }

    fun continueAsGuest() {
        _currentUser.value = AuthUser("Convidado", "")
        _isLoggedIn.value = false
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        clearError()
    }
}