package com.arquitectura.ecommerce.controller;

import com.arquitectura.ecommerce.dto.ProductoDTO;
import com.arquitectura.ecommerce.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        return new ResponseEntity<>(productoService.crearProducto(productoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductoDTO>> listarProductos(Pageable pageable) {
        return ResponseEntity.ok(productoService.obtenerTodosLosProductos(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProductoDTO>> buscarProductos(@RequestParam String nombre, Pageable pageable) {
        return ResponseEntity.ok(productoService.buscarProductosPorNombre(nombre, pageable));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Page<ProductoDTO>> obtenerProductosPorCategoria(@PathVariable Long categoriaId, Pageable pageable) {
        return ResponseEntity.ok(productoService.obtenerProductosPorCategoria(categoriaId, pageable));
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosDestacados() {
        return ResponseEntity.ok(productoService.obtenerProductosDestacados());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
