package com.arquitectura.ecommerce.service;

import com.arquitectura.ecommerce.dto.CarritoDTO;
import com.arquitectura.ecommerce.dto.ItemCarritoDTO;

public interface CarritoService {

    CarritoDTO obtenerCarrito(Long usuarioId);

    CarritoDTO agregarItem(Long usuarioId, ItemCarritoDTO itemCarritoDTO);

    CarritoDTO actualizarCantidad(Long usuarioId, Long productoId, Integer cantidad);

    CarritoDTO eliminarItem(Long usuarioId, Long productoId);

    void vaciarCarrito(Long usuarioId);
}
