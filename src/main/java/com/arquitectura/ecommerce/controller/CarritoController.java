package com.arquitectura.ecommerce.controller;

import com.arquitectura.ecommerce.dto.CarritoDTO;
import com.arquitectura.ecommerce.dto.ItemCarritoDTO;
import com.arquitectura.ecommerce.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<CarritoDTO> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<CarritoDTO> agregarItemAlCarrito(@PathVariable Long usuarioId, @Valid @RequestBody ItemCarritoDTO itemCarritoDTO) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, itemCarritoDTO));
    }

    @PutMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<CarritoDTO> actualizarCantidadItem(@PathVariable Long usuarioId, @PathVariable Long productoId, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(usuarioId, productoId, cantidad));
    }

    @DeleteMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<CarritoDTO> eliminarItemDelCarrito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.eliminarItem(usuarioId, productoId));
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
