   // en InventarioViewModel

enum class TipoMovimiento { ENTRADA, SALIDA_VENTA, SALIDA_CONSUMO, AJUSTE }

data class Movimiento(
    val id: Int,
    val codigoProducto: String,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val motivo: String,
    val fecha: String,
    val usuario: String
)