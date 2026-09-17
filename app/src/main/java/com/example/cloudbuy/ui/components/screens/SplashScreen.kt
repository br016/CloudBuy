package com.example.cloudbuy.ui.components.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudbuy.ui.components.theme.CloudBlue
import com.example.cloudbuy.ui.components.theme.CloudWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(800, easing = EaseOutBounce))
        delay(1500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CloudBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Cloud,
                contentDescription = null,
                tint = CloudWhite,
                modifier = Modifier.size(100.dp).scale(scale.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "CloudBuy",
                color = CloudWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Compre nas nuvens ☁️",
                color = CloudWhite.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}