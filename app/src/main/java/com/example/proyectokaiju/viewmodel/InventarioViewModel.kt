package com.example.proyectokaiju.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectokaiju.model.*
import com.example.proyectokaiju.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InventarioViewModel : ViewModel() {
    private val repo = InventarioRepository()

    // TODO: cuando exista el login, este valor viene de SesionViewModel
    private val usuarioActual = "Ana Admin"

    private val _productos = MutableStateFlow(repo.obtenerProductos())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _formProducto = MutableStateFlow(FormularioProductoEstado())
    val formProducto: StateFlow<FormularioProductoEstado> = _formProducto.asStateFlow()

    private val _formMovimiento = MutableStateFlow(FormularioMovimientoEstado())
    val formMovimiento: StateFlow<FormularioMovimientoEstado> = _formMovimiento.asStateFlow()

    val categorias = listOf("Ropa", "Calzado", "Accesorios", "Aseo", "Papelería")

    fun enAlerta(p: Producto) = p.stockActual <= p.stockMinimo   // RN-06

    // ---------- Formulario de producto ----------
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
        _formProducto.value = FormularioProductoEstado()
        return true
    }

    fun limpiarFormularioProducto() {
        _formProducto.value = FormularioProductoEstado()
    }

    // ---------- Formulario de movimiento ----------
    fun seleccionarProductoMov(codigo: String) = _formMovimiento.update { it.copy(codigoProducto = codigo) }
    fun seleccionarTipoMov(tipo: TipoMovimiento) = _formMovimiento.update { it.copy(tipo = tipo) }
    fun actualizarCantidadMov(v: String) = _formMovimiento.update { it.copy(cantidad = v) }
    fun actualizarMotivoMov(v: String) = _formMovimiento.update { it.copy(motivo = v) }
    fun actualizarAjusteResta(v: Boolean) = _formMovimiento.update { it.copy(ajusteResta = v) }

    private fun descuentaStock(e: FormularioMovimientoEstado) = when (e.tipo) {
        TipoMovimiento.SALIDA_VENTA, TipoMovimiento.SALIDA_CONSUMO -> true
        TipoMovimiento.AJUSTE -> e.ajusteResta
        else -> false
    }

    fun validarFormularioMovimiento(): Boolean {
        val e = _formMovimiento.value
        val producto = _productos.value.find { it.codigo == e.codigoProducto }
        val cantidad = e.cantidad.toIntOrNull()

        val errorProducto = if (producto == null) "Elige un producto" else null
        val errorTipo = if (e.tipo == null) "Elige un tipo de movimiento" else null
        val errorCantidad = when {
            cantidad == null -> "La cantidad debe ser un número entero"
            cantidad <= 0 -> "La cantidad debe ser mayor que cero"
            producto != null && descuentaStock(e) && cantidad > producto.stockActual ->
                "No hay stock suficiente (disponible: ${producto.stockActual})"   // RN-01
            else -> null
        }
        val errorMotivo =
            if (e.tipo == TipoMovimiento.AJUSTE && e.motivo.isBlank())
                "El ajuste exige un motivo"                                       // RN-04
            else null

        _formMovimiento.update {
            it.copy(errores = ErroresMovimiento(errorProducto, errorTipo, errorCantidad, errorMotivo))
        }
        return listOf(errorProducto, errorTipo, errorCantidad, errorMotivo).all { it == null }
    }

    fun registrarMovimiento(): Boolean {
        if (!validarFormularioMovimiento()) return false
        val e = _formMovimiento.value
        val producto = _productos.value.first { it.codigo == e.codigoProducto }
        val cantidad = e.cantidad.toInt()
        val delta = if (descuentaStock(e)) -cantidad else cantidad

        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        // RN-05: el movimiento queda registrado y no se edita ni se borra
        val movimiento = Movimiento(
            id = repo.siguienteIdMovimiento(),
            codigoProducto = producto.codigo,
            tipo = e.tipo!!,
            cantidad = if (e.tipo == TipoMovimiento.AJUSTE) delta else cantidad,
            motivo = e.motivo.trim(),
            fecha = fecha,
            usuario = usuarioActual
        )
        repo.agregarMovimiento(movimiento)
        repo.guardarProducto(producto.copy(stockActual = producto.stockActual + delta))
        _productos.value = repo.obtenerProductos()
        _formMovimiento.value = FormularioMovimientoEstado()
        return true
    }

    fun movimientosDe(codigo: String) = repo.obtenerMovimientos(codigo)
}