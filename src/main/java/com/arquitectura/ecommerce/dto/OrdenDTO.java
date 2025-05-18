package com.arquitectura.ecommerce.dto;

import com.arquitectura.ecommerce.model.Orden;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenDTO {
    private Long id;
    private String numeroOrden;
    private LocalDateTime fechaCreacion;
    private BigDecimal total;
    private Orden.EstadoOrden estado;
    private Long usuarioId;
    private String nombreCompleto;
    private List<DetalleOrdenDTO> detalles;
}