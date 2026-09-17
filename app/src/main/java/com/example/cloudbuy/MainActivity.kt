package com.example.cloudbuy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cloudbuy.ui.components.navigation.AppNavigation
import com.example.cloudbuy.ui.components.theme.CloudBuyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CloudBuyTheme {
                AppNavigation()
            }
        }
    }
}