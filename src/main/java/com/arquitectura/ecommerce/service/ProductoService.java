package com.arquitectura.ecommerce.service;

import com.arquitectura.ecommerce.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductoService {

    ProductoDTO crearProducto(ProductoDTO productoDTO);

    ProductoDTO obtenerProductoPorId(Long id);

    Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable);

    Page<ProductoDTO> buscarProductosPorNombre(String nombre, Pageable pageable);

    Page<ProductoDTO> obtenerProductosPorCategoria(Long categoriaId, Pageable pageable);

    ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO);

    void eliminarProducto(Long id);

    List<ProductoDTO> obtenerProductosDestacados();
}
