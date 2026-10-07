package com.example.proyectokaiju.repository
import com.example.proyectokaiju.model.*

class InventarioRepository {
    private val productos = DatosDePrueba.productos.toMutableList()
    private val movimientos = mutableListOf<Movimiento>()

    fun obtenerProductos(): List<Producto> = productos.toList()
    fun obtenerMovimientos(codigo: String) =
        movimientos.filter { it.codigoProducto == codigo }.sortedByDescending { it.fecha }

    fun guardarProducto(p: Producto) {
        val i = productos.indexOfFirst { it.codigo == p.codigo }
        if (i >= 0) productos[i] = p else productos.add(p)
    }

    fun agregarMovimiento(m: Movimiento) { movimientos.add(m) }
    fun usuarios() = DatosDePrueba.usuarios
}