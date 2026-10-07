package com.example.proyectokaiju.model
data class Producto(
    val codigo: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val tipo: String,
    val precio: Int,          // pesos chilenos, sin decimales
    val stockActual: Int,
    val stockMinimo: Int,
    val detalle: String = ""  // talla, color, lote, vencimiento
)