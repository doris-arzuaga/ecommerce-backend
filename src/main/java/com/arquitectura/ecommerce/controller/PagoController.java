package com.arquitectura.ecommerce.controller;

import com.arquitectura.ecommerce.dto.PagoDTO;
import com.arquitectura.ecommerce.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/orden/{ordenId}")
    public ResponseEntity<PagoDTO> procesarPago(@PathVariable Long ordenId, @Valid @RequestBody PagoDTO pagoDTO) {
        return new ResponseEntity<>(pagoService.procesarPago(ordenId, pagoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<PagoDTO> obtenerPagoPorOrdenId(@PathVariable Long ordenId) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorOrdenId(ordenId));
    }
}
