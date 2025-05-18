package com.arquitectura.ecommerce.service.impl;

import com.arquitectura.ecommerce.dto.CarritoDTO;
import com.arquitectura.ecommerce.dto.ItemCarritoDTO;
import com.arquitectura.ecommerce.exception.ResourceNotFoundException;
import com.arquitectura.ecommerce.model.Producto;
import com.arquitectura.ecommerce.repository.ProductoRepository;
import com.arquitectura.ecommerce.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CarritoServiceImpl implements CarritoService {

    // Simulamos el carrito en memoria para simplificar el MVP
    // En una implementación real, esto podría almacenarse en Redis o en la base de datos
    private final Map<Long, Map<Long, ItemCarritoDTO>> carritosUsuarios = new ConcurrentHashMap<>();

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public CarritoDTO obtenerCarrito(Long usuarioId) {
        Map<Long, ItemCarritoDTO> itemsCarrito = carritosUsuarios.getOrDefault(usuarioId, new HashMap<>());

        List<ItemCarritoDTO> items = new ArrayList<>(itemsCarrito.values());
        BigDecimal total = calcularTotal(items);

        return new CarritoDTO(items, total);
    }

    @Override
    public CarritoDTO agregarItem(Long usuarioId, ItemCarritoDTO itemCarritoDTO) {
        Producto producto = productoRepository.findById(itemCarritoDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + itemCarritoDTO.getProductoId()));

        // Validar stock disponible
        if (producto.getStock() < itemCarritoDTO.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        Map<Long, ItemCarritoDTO> itemsCarrito = carritosUsuarios.computeIfAbsent(usuarioId, k -> new HashMap<>());

        // Si el producto ya está en el carrito, actualizar cantidad
        if (itemsCarrito.containsKey(itemCarritoDTO.getProductoId())) {
            ItemCarritoDTO itemExistente = itemsCarrito.get(itemCarritoDTO.getProductoId());
            Integer nuevaCantidad = itemExistente.getCantidad() + itemCarritoDTO.getCantidad();

            // Validar stock con la nueva cantidad
            if (producto.getStock() < nuevaCantidad) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            itemExistente.setCantidad(nuevaCantidad);
            itemExistente.setSubtotal(itemExistente.getPrecioUnitario().multiply(new BigDecimal(nuevaCantidad)));
        } else {
            // Completar información del item
            itemCarritoDTO.setProductoNombre(producto.getNombre());
            itemCarritoDTO.setImagen(producto.getImagen());
            itemCarritoDTO.setPrecioUnitario(producto.getPrecio());
            itemCarritoDTO.setSubtotal(producto.getPrecio().multiply(new BigDecimal(itemCarritoDTO.getCantidad())));

            itemsCarrito.put(itemCarritoDTO.getProductoId(), itemCarritoDTO);
        }

        return obtenerCarrito(usuarioId);
    }

    @Override
    public CarritoDTO actualizarCantidad(Long usuarioId, Long productoId, Integer cantidad) {
        Map<Long, ItemCarritoDTO> itemsCarrito = carritosUsuarios.getOrDefault(usuarioId, new HashMap<>());

        if (!itemsCarrito.containsKey(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado en el carrito");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

        // Validar stock disponible
        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        ItemCarritoDTO item = itemsCarrito.get(productoId);
        item.setCantidad(cantidad);
        item.setSubtotal(item.getPrecioUnitario().multiply(new BigDecimal(cantidad)));

        return obtenerCarrito(usuarioId);
    }

    @Override
    public CarritoDTO eliminarItem(Long usuarioId, Long productoId) {
        Map<Long, ItemCarritoDTO> itemsCarrito = carritosUsuarios.getOrDefault(usuarioId, new HashMap<>());

        if (!itemsCarrito.containsKey(productoId)) {
            throw new ResourceNotFoundException("Producto no encontrado en el carrito");
        }

        itemsCarrito.remove(productoId);

        return obtenerCarrito(usuarioId);
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        carritosUsuarios.remove(usuarioId);
    }

    private BigDecimal calcularTotal(List<ItemCarritoDTO> items) {
        return items.stream()
                .map(ItemCarritoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}