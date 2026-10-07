package com.example.proyectokaiju.model

data class FormularioProductoEstado(
    val codigo: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val precio: String = "",        // texto mientras se escribe
    val stockMinimo: String = "",   // texto mientras se escribe
    val errores: ErroresProducto = ErroresProducto()
)

data class ErroresProducto(
    val errorCodigo: String? = null,
    val errorNombre: String? = null,
    val errorCategoria: String? = null,
    val errorPrecio: String? = null,
    val errorStockMinimo: String? = null
)