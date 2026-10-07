package com.example.proyectokaiju.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectokaiju.model.Producto
import com.example.proyectokaiju.repository.InventarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventarioViewModel : ViewModel() {
    private val repo = InventarioRepository()

    private val _productos = MutableStateFlow(repo.obtenerProductos())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    fun enAlerta(p: Producto) = p.stockActual <= p.stockMinimo   // RN-06
}