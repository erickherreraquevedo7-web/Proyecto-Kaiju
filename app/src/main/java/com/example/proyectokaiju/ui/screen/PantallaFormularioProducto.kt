package com.example.proyectokaiju.ui.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectokaiju.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioProducto(
    viewModel: InventarioViewModel,
    onGuardado: () -> Unit
) {
    val estado by viewModel.formProducto.collectAsState()
    val err = estado.errores

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nuevo producto") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = estado.codigo,
                onValueChange = viewModel::actualizarCodigo,
                label = { Text("Código") },
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
                trailingIcon = {
                    if (err.errorCodigo != null) Icon(Icons.Filled.Warning, contentDescription = "Error")
                },
                isError = err.errorCodigo != null,
                supportingText = { err.errorCodigo?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::actualizarNombre,
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                trailingIcon = {
                    if (err.errorNombre != null) Icon(Icons.Filled.Warning, contentDescription = "Error")
                },
                isError = err.errorNombre != null,
                supportingText = { err.errorNombre?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.categorias.forEach { cat ->
                    FilterChip(
                        selected = estado.categoria == cat,
                        onClick = { viewModel.actualizarCategoria(cat) },
                        label = { Text(cat) }
                    )
                }
            }
            err.errorCategoria?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            OutlinedTextField(
                value = estado.precio,
                onValueChange = viewModel::actualizarPrecio,
                label = { Text("Precio (CLP)") },
                leadingIcon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
                trailingIcon = {
                    if (err.errorPrecio != null) Icon(Icons.Filled.Warning, contentDescription = "Error")
                },
                isError = err.errorPrecio != null,
                supportingText = { err.errorPrecio?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.stockMinimo,
                onValueChange = viewModel::actualizarStockMinimo,
                label = { Text("Stock mínimo") },
                leadingIcon = { Icon(Icons.Filled.Warning, contentDescription = null) },
                isError = err.errorStockMinimo != null,
                supportingText = { err.errorStockMinimo?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { if (viewModel.guardarProducto()) onGuardado() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar producto")
            }
        }
    }
}