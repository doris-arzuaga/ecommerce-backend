package com.arquitectura.ecommerce.service;

import com.arquitectura.ecommerce.dto.OrdenDTO;
import com.arquitectura.ecommerce.model.Orden;

import java.util.List;

public interface OrdenService {

    OrdenDTO crearOrden(Long usuarioId);

    OrdenDTO obtenerOrdenPorId(Long id);

    List<OrdenDTO> obtenerOrdenesPorUsuario(Long usuarioId);

    OrdenDTO actualizarEstadoOrden(Long id, Orden.EstadoOrden estado);

    void cancelarOrden(Long id);
}