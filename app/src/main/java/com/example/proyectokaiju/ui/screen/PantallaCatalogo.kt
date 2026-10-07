package com.example.proyectokaiju.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.proyectokaiju.ui.components.TarjetaProducto
import com.example.proyectokaiju.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCatalogo(
    viewModel: InventarioViewModel,
    onProductoClick: (String) -> Unit,
    onAgregarClick: () -> Unit
) {
    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Kaiju · Catálogo") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(productos) { producto ->
                TarjetaProducto(
                    producto = producto,
                    enAlerta = viewModel.enAlerta(producto),
                    onClick = { onProductoClick(producto.codigo) }
                )
            }
        }
    }
}