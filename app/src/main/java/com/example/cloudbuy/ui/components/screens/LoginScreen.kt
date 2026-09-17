package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    val error by authViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Cloud, null, tint = CloudBlue, modifier = Modifier.size(72.dp))
        Text("CloudBuy", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = CloudBlue)
        Text("Entre na sua conta", color = CloudLightText)
        Spacer(Modifier.height(24.dp))

        if (error != null) {
            Text(error ?: "", color = CloudRed, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; authViewModel.clearError() },
            label = { Text("E-mail") },
            leadingIcon = { Icon(Icons.Default.Email, null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; authViewModel.clearError() },
            label = { Text("Senha") },
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            trailingIcon = {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { if (authViewModel.login(email, password)) onLoginSuccess() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CloudBlue)
        ) {
            Text("Entrar", fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = onNavigateToRegister) {
            Text("Não tem conta? Cadastre-se", color = CloudBlue)
        }
        OutlinedButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continuar sem login")
        }
        Spacer(Modifier.height(8.dp))
        Text("Demo: admin@cloudbuy.com / 123456", fontSize = 11.sp, color = CloudLightText)
    }
}