package com.example.proyectokaiju.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectokaiju.model.ErroresProducto
import com.example.proyectokaiju.model.FormularioProductoEstado
import com.example.proyectokaiju.model.Producto
import com.example.proyectokaiju.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InventarioViewModel : ViewModel() {
    private val repo = InventarioRepository()

    private val _productos = MutableStateFlow(repo.obtenerProductos())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _formProducto = MutableStateFlow(FormularioProductoEstado())
    val formProducto: StateFlow<FormularioProductoEstado> = _formProducto.asStateFlow()

    val categorias = listOf("Ropa", "Calzado", "Accesorios", "Aseo", "Papelería")

    fun enAlerta(p: Producto) = p.stockActual <= p.stockMinimo   // RN-06

    // --- Formulario de producto ---
    fun actualizarCodigo(v: String) = _formProducto.update { it.copy(codigo = v) }
    fun actualizarNombre(v: String) = _formProducto.update { it.copy(nombre = v) }
    fun actualizarCategoria(v: String) = _formProducto.update { it.copy(categoria = v) }
    fun actualizarPrecio(v: String) = _formProducto.update { it.copy(precio = v) }
    fun actualizarStockMinimo(v: String) = _formProducto.update { it.copy(stockMinimo = v) }

    fun validarFormularioProducto(): Boolean {
        val e = _formProducto.value
        val codigo = e.codigo.trim()
        val precio = e.precio.toIntOrNull()
        val stockMin = e.stockMinimo.toIntOrNull()

        val errorCodigo = when {
            codigo.isBlank() -> "El código es obligatorio"
            _productos.value.any { it.codigo.equals(codigo, ignoreCase = true) } ->
                "Ese código ya existe"                                   // RN-03
            else -> null
        }
        val errorNombre =
            if (e.nombre.trim().length < 3) "Mínimo 3 caracteres" else null
        val errorCategoria =
            if (e.categoria.isBlank()) "Elige una categoría" else null
        val errorPrecio = when {
            precio == null -> "El precio debe ser un número entero"
            precio <= 0 -> "El precio debe ser mayor que cero"           // RN-02
            else -> null
        }
        val errorStockMin = when {
            stockMin == null -> "El stock mínimo debe ser un número entero"
            stockMin < 0 -> "El stock mínimo no puede ser negativo"      // RN-02
            else -> null
        }

        _formProducto.update {
            it.copy(
                errores = ErroresProducto(
                    errorCodigo, errorNombre, errorCategoria, errorPrecio, errorStockMin
                )
            )
        }
        return listOf(errorCodigo, errorNombre, errorCategoria, errorPrecio, errorStockMin)
            .all { it == null }
    }

    fun guardarProducto(): Boolean {
        if (!validarFormularioProducto()) return false
        val e = _formProducto.value
        val nuevo = Producto(
            codigo = e.codigo.trim().uppercase(),
            nombre = e.nombre.trim(),
            descripcion = "",
            categoria = e.categoria,
            tipo = e.categoria,
            precio = e.precio.toInt(),
            stockActual = 0,
            stockMinimo = e.stockMinimo.toInt()
        )
        repo.guardarProducto(nuevo)
        _productos.value = repo.obtenerProductos()
        _formProducto.value = FormularioProductoEstado()   // limpia el formulario
        return true
    }

    fun limpiarFormularioProducto() {
        _formProducto.value = FormularioProductoEstado()
    }
}