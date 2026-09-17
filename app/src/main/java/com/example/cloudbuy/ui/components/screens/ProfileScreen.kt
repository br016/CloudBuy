package com.example.cloudbuy.ui.components.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.ui.components.theme.*
import com.example.cloudbuy.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onMyOrders: () -> Unit,
    onLoginClick: () -> Unit,
    onLogout: () -> Unit
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CloudBlue, titleContentColor = CloudWhite)
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(Modifier.size(96.dp).clip(CircleShape), color = CloudBlueLight) {
                Icon(Icons.Default.Person, null, Modifier.padding(20.dp), tint = CloudBlue)
            }
            Spacer(Modifier.height(12.dp))
            if (isLoggedIn && user != null) {
                Text(user!!.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(user!!.email, color = CloudLightText)
            } else {
                Text("Visitante", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Button(onClick = onLoginClick, colors = ButtonDefaults.buttonColors(containerColor = CloudBlue)) {
                    Text("Fazer login")
                }
            }
            Spacer(Modifier.height(24.dp))
            Card(
                onClick = onMyOrders,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CloudGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Receipt, null, tint = CloudBlue)
                    Spacer(Modifier.width(12.dp))
                    Text("Meus Pedidos", Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
            Spacer(Modifier.weight(1f))
            if (isLoggedIn) {
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CloudRed)
                ) {
                    Text("Sair")
                }
            }
        }
    }
}