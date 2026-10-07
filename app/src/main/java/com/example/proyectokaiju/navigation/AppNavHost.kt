

package com.example.proyectokaiju.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectokaiju.ui.screen.PantallaCatalogo
import com.example.proyectokaiju.viewmodel.InventarioViewModel

@Composable
private fun PantallaTitulo(titulo: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(titulo) }
}

@Composable
fun AppNavHost(viewModel: InventarioViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = rutaActual == Rutas.Catalogo.ruta,
                    onClick = { navController.navigate(Rutas.Catalogo.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("Catálogo") }
                )
                NavigationBarItem(
                    selected = rutaActual == Rutas.Movimiento.ruta,
                    onClick = { navController.navigate(Rutas.Movimiento.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    label = { Text("Movimiento") }
                )
                NavigationBarItem(
                    selected = rutaActual == Rutas.Alertas.ruta,
                    onClick = { navController.navigate(Rutas.Alertas.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.Warning, contentDescription = null) },
                    label = { Text("Alertas") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Catalogo.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.Login.ruta) { PantallaTitulo("Login") }
            composable(Rutas.Catalogo.ruta) {
                PantallaCatalogo(viewModel) { codigo ->
                    navController.navigate(Rutas.Detalle.crear(codigo))
                }
            }
            composable(Rutas.Detalle.ruta) { PantallaTitulo("Detalle del producto") }
            composable(Rutas.FormularioProducto.ruta) { PantallaTitulo("Formulario de producto") }
            composable(Rutas.Movimiento.ruta) { PantallaTitulo("Registrar movimiento") }
            composable(Rutas.Alertas.ruta) { PantallaTitulo("Alertas de stock") }
        }
    }
}