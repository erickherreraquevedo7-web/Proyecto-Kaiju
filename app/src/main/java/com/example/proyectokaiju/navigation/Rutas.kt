package com.example.proyectokaiju.navigation


sealed class Rutas(val ruta: String) {
    object Login : Rutas("login")
    object Catalogo : Rutas("catalogo")
    object Detalle : Rutas("detalle/{codigo}") {
        fun crear(codigo: String) = "detalle/$codigo"
    }
    object FormularioProducto : Rutas("formulario_producto")
    object Movimiento : Rutas("movimiento")
    object Alertas : Rutas("alertas")
}