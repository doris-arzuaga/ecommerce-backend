package com.arquitectura.ecommerce.controller;

import com.arquitectura.ecommerce.dto.OrdenDTO;
import com.arquitectura.ecommerce.model.Orden;
import com.arquitectura.ecommerce.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<OrdenDTO> crearOrden(@PathVariable Long usuarioId) {
        return new ResponseEntity<>(ordenService.crearOrden(usuarioId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> obtenerOrden(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerOrdenPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<OrdenDTO>> obtenerOrdenesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.obtenerOrdenesPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenDTO> actualizarEstadoOrden(@PathVariable Long id, @RequestParam Orden.EstadoOrden estado) {
        return ResponseEntity.ok(ordenService.actualizarEstadoOrden(id, estado));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarOrden(@PathVariable Long id) {
        ordenService.cancelarOrden(id);
        return ResponseEntity.noContent().build();
    }
}
