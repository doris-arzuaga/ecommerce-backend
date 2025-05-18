package com.arquitectura.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {
    private Long productoId;
    private String productoNombre;
    private String imagen;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}