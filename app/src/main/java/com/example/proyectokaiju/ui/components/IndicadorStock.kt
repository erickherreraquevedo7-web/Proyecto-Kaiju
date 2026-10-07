package com.example.proyectokaiju.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun IndicadorStock(stockActual: Int, enAlerta: Boolean) {
    Text(
        text = if (enAlerta) "⚠ Stock bajo: $stockActual" else "Stock: $stockActual",
        color = if (enAlerta) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onSurface
    )
}