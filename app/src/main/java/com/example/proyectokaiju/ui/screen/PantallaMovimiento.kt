package com.example.proyectokaiju.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectokaiju.model.TipoMovimiento
import com.example.proyectokaiju.viewmodel.InventarioViewModel

private fun etiqueta(t: TipoMovimiento) = when (t) {
    TipoMovimiento.ENTRADA -> "Entrada"
    TipoMovimiento.SALIDA_VENTA -> "Salida (venta)"
    TipoMovimiento.SALIDA_CONSUMO -> "Salida (consumo)"
    TipoMovimiento.AJUSTE -> "Ajuste"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMovimiento(
    viewModel: InventarioViewModel,
    onGuardado: () -> Unit
) {
    val estado by viewModel.formMovimiento.collectAsState()
    val productos by viewModel.productos.collectAsState()
    val err = estado.errores
    var menuAbierto by remember { mutableStateOf(false) }
    val productoSel = productos.find { it.codigo == estado.codigoProducto }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registrar movimiento") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Producto", style = MaterialTheme.typography.labelLarge)
            Box {
                OutlinedButton(
                    onClick = { menuAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        productoSel?.let { "${it.codigo} · ${it.nombre} (stock ${it.stockActual})" }
                            ?: "Elegir producto"
                    )
                }
                DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                    productos.forEach { p ->
                        DropdownMenuItem(
                            text = { Text("${p.codigo} · ${p.nombre} (stock ${p.stockActual})") },
                            onClick = {
                                viewModel.seleccionarProductoMov(p.codigo)
                                menuAbierto = false
                            }
                        )
                    }
                }
            }
            err.errorProducto?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            Text("Tipo de movimiento", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TipoMovimiento.values().forEach { tipo ->
                    FilterChip(
                        selected = estado.tipo == tipo,
                        onClick = { viewModel.seleccionarTipoMov(tipo) },
                        label = { Text(etiqueta(tipo)) }
                    )
                }
            }
            err.errorTipo?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            if (estado.tipo == TipoMovimiento.AJUSTE) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = estado.ajusteResta,
                        onCheckedChange = viewModel::actualizarAjusteResta
                    )
                    Text(
                        if (estado.ajusteResta) "  El ajuste resta stock" else "  El ajuste suma stock"
                    )
                }
            }

            OutlinedTextField(
                value = estado.cantidad,
                onValueChange = viewModel::actualizarCantidadMov,
                label = { Text("Cantidad") },
                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null) },
                trailingIcon = {
                    if (err.errorCantidad != null) Icon(Icons.Filled.Warning, contentDescription = "Error")
                },
                isError = err.errorCantidad != null,
                supportingText = { err.errorCantidad?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.motivo,
                onValueChange = viewModel::actualizarMotivoMov,
                label = { Text("Motivo (obligatorio en ajustes)") },
                leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                trailingIcon = {
                    if (err.errorMotivo != null) Icon(Icons.Filled.Warning, contentDescription = "Error")
                },
                isError = err.errorMotivo != null,
                supportingText = { err.errorMotivo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { if (viewModel.registrarMovimiento()) onGuardado() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar movimiento")
            }
        }
    }
}