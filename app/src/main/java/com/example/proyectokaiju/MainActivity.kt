package com.example.proyectokaiju

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyectokaiju.navigation.AppNavHost
import com.example.proyectokaiju.ui.theme.ProyectoKaijuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoKaijuTheme {
                AppNavHost()
            }
        }
    }
}