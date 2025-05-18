package com.arquitectura.ecommerce.service.impl;

import com.arquitectura.ecommerce.dto.PagoDTO;
import com.arquitectura.ecommerce.exception.ResourceNotFoundException;
import com.arquitectura.ecommerce.model.Orden;
import com.arquitectura.ecommerce.model.Pago;
import com.arquitectura.ecommerce.repository.OrdenRepository;
import com.arquitectura.ecommerce.repository.PagoRepository;
import com.arquitectura.ecommerce.service.OrdenService;
import com.arquitectura.ecommerce.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagoServiceImpl implements PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private OrdenService ordenService;

    @Override
    @Transactional
    public PagoDTO procesarPago(Long ordenId, PagoDTO pagoDTO) {
        // Buscar la orden sin cargar los detalles
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + ordenId));

        // Verificar si la orden ya tiene un pago
        boolean existePago = pagoRepository.existsByOrdenId(ordenId);
        if (existePago) {
            throw new IllegalArgumentException("La orden ya tiene un pago asociado");
        }

        // Simulación de procesamiento de pago
        boolean pagoExitoso = simularProcesamiento(pagoDTO);

        Pago pago = new Pago();
        pago.setMonto(orden.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodo(pagoDTO.getMetodo());
        pago.setNumeroTransaccion(generarNumeroTransaccion());
        pago.setEstado(pagoExitoso ? Pago.EstadoPago.COMPLETADO : Pago.EstadoPago.FALLIDO);
        pago.setOrden(orden);

        Pago pagoGuardado = pagoRepository.save(pago);

        // Si el pago fue exitoso, actualizar el estado de la orden
        if (pagoExitoso) {
            ordenService.actualizarEstadoOrden(ordenId, Orden.EstadoOrden.PAGADO);
        }

        return convertirADTO(pagoGuardado);
    }

    @Override
    public PagoDTO obtenerPagoPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));

        return convertirADTO(pago);
    }

    @Override
    public PagoDTO obtenerPagoPorOrdenId(Long ordenId) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + ordenId));

        Pago pago = pagoRepository.findByOrden(orden)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado para la orden con id: " + ordenId));

        return convertirADTO(pago);
    }

    private boolean simularProcesamiento(PagoDTO pagoDTO) {
        // Simulamos que todos los pagos son exitosos para el MVP
        return true;
    }

    private String generarNumeroTransaccion() {
        return "TRX-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    private PagoDTO convertirADTO(Pago pago) {
        PagoDTO pagoDTO = new PagoDTO();
        pagoDTO.setId(pago.getId());
        pagoDTO.setMonto(pago.getMonto());
        pagoDTO.setFechaPago(pago.getFechaPago());
        pagoDTO.setMetodo(pago.getMetodo());
        pagoDTO.setNumeroTransaccion(pago.getNumeroTransaccion());
        pagoDTO.setEstado(pago.getEstado());
        pagoDTO.setOrdenId(pago.getOrden().getId());
        return pagoDTO;
    }
}
