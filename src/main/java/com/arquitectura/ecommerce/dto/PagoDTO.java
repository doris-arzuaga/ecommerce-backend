package com.arquitectura.ecommerce.dto;

import com.arquitectura.ecommerce.model.Pago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String metodo;
    private String numeroTransaccion;
    private Pago.EstadoPago estado;
    private Long ordenId;

    private Map<String, String> datosPago;
}