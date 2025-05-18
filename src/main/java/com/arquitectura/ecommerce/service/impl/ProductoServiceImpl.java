package com.arquitectura.ecommerce.service.impl;

import com.arquitectura.ecommerce.dto.ProductoDTO;
import com.arquitectura.ecommerce.exception.ResourceNotFoundException;
import com.arquitectura.ecommerce.model.Categoria;
import com.arquitectura.ecommerce.model.Producto;
import com.arquitectura.ecommerce.repository.CategoriaRepository;
import com.arquitectura.ecommerce.repository.ProductoRepository;
import com.arquitectura.ecommerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public ProductoDTO crearProducto(ProductoDTO productoDTO) {
        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagen(productoDTO.getImagen());
        producto.setCategoria(categoria);

        Producto productoGuardado = productoRepository.save(producto);

        return convertirADTO(productoGuardado);
    }

    @Override
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        return convertirADTO(producto);
    }

    @Override
    public Page<ProductoDTO> obtenerTodosLosProductos(Pageable pageable) {
        Page<Producto> productos = productoRepository.findAll(pageable);
        return productos.map(this::convertirADTO);
    }

    @Override
    public Page<ProductoDTO> buscarProductosPorNombre(String nombre, Pageable pageable) {
        Page<Producto> productos = productoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        return productos.map(this::convertirADTO);
    }

    @Override
    public Page<ProductoDTO> obtenerProductosPorCategoria(Long categoriaId, Pageable pageable) {
        Page<Producto> productos = productoRepository.findByCategoriaId(categoriaId, pageable);
        return productos.map(this::convertirADTO);
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        Categoria categoria = categoriaRepository.findById(productoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + productoDTO.getCategoriaId()));

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagen(productoDTO.getImagen());
        producto.setCategoria(categoria);

        Producto productoActualizado = productoRepository.save(producto);

        return convertirADTO(productoActualizado);
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }

        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoDTO> obtenerProductosDestacados() {
        // Una implementación simple para obtener productos destacados (los 8 primeros)
        Page<Producto> productosPage = productoRepository.findAll(Pageable.ofSize(8));
        return productosPage.getContent().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setStock(producto.getStock());
        productoDTO.setImagen(producto.getImagen());
        productoDTO.setCategoriaId(producto.getCategoria().getId());
        productoDTO.setCategoriaNombre(producto.getCategoria().getNombre());
        return productoDTO;
    }
}
