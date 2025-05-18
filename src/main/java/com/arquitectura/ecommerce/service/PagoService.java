package com.arquitectura.ecommerce.service;

import com.arquitectura.ecommerce.dto.PagoDTO;
import com.arquitectura.ecommerce.model.Pago;

public interface PagoService {

    PagoDTO procesarPago(Long ordenId, PagoDTO pagoDTO);

    PagoDTO obtenerPagoPorId(Long id);

    PagoDTO obtenerPagoPorOrdenId(Long ordenId);
}
