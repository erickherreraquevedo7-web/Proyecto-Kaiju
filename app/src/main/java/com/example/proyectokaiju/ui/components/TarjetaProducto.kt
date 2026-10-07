package com.example.proyectokaiju.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectokaiju.model.Producto

@Composable
fun TarjetaProducto(
    producto: Producto,
    enAlerta: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(producto.nombre, fontWeight = FontWeight.Bold)
            Text("${producto.codigo} · ${producto.categoria}")
            Text("$${producto.precio}")
            IndicadorStock(producto.stockActual, enAlerta)
        }
    }
}