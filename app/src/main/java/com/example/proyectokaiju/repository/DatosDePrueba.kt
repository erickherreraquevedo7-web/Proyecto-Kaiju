package com.example.proyectokaiju.repository

import com.example.proyectokaiju.model.*

object DatosDePrueba {
    val usuarios = listOf(
        Usuario(1, "Ana Admin", Rol.ADMINISTRADOR),
        Usuario(2, "Vicente Ventas", Rol.VENDEDOR),
        Usuario(3, "Ingrid Inventario", Rol.ENCARGADO_INVENTARIO)
    )

    val productos = listOf(
        Producto("P001", "Polera básica", "Algodón", "Ropa", "Ropa", 9990, 25, 5, "Talla M, negro"),
        Producto("P002", "Jeans slim", "Denim", "Ropa", "Ropa", 24990, 3, 5, "Talla 32, azul"),
        Producto("P003", "Zapatillas urbanas", "Running", "Calzado", "Calzado", 39990, 8, 4, "Talla 42"),
        Producto("P004", "Gorro lana", "Invierno", "Accesorios", "Ropa", 5990, 2, 3, "Gris"),
        Producto("P005", "Mochila 20L", "Impermeable", "Accesorios", "Accesorio", 19990, 12, 4),
        Producto("P006", "Detergente 1L", "Multiuso", "Aseo", "Insumo", 3490, 40, 10, "Lote L-2026-01"),
        Producto("P007", "Cloro 1L", "Desinfectante", "Aseo", "Insumo", 1990, 4, 10, "Vence 2027-03"),
        Producto("P008", "Cuaderno A4", "100 hojas", "Papelería", "Insumo", 2490, 30, 8),
        Producto("P009", "Lápiz pasta caja", "x12", "Papelería", "Insumo", 3990, 6, 6),
        Producto("P010", "Polerón canguro", "Con capucha", "Ropa", "Ropa", 29990, 15, 5, "Talla L, gris")
    )
}