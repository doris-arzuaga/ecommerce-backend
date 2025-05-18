package com.arquitectura.ecommerce.service.impl;

import com.arquitectura.ecommerce.dto.CarritoDTO;
import com.arquitectura.ecommerce.dto.DetalleOrdenDTO;
import com.arquitectura.ecommerce.dto.ItemCarritoDTO;
import com.arquitectura.ecommerce.dto.OrdenDTO;
import com.arquitectura.ecommerce.exception.ResourceNotFoundException;
import com.arquitectura.ecommerce.model.DetalleOrden;
import com.arquitectura.ecommerce.model.Orden;
import com.arquitectura.ecommerce.model.Producto;
import com.arquitectura.ecommerce.model.Usuario;
import com.arquitectura.ecommerce.repository.DetalleOrdenRepository;
import com.arquitectura.ecommerce.repository.OrdenRepository;
import com.arquitectura.ecommerce.repository.ProductoRepository;
import com.arquitectura.ecommerce.repository.UsuarioRepository;
import com.arquitectura.ecommerce.service.CarritoService;
import com.arquitectura.ecommerce.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdenServiceImpl implements OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoService carritoService;

    @Override
    @Transactional
    public OrdenDTO crearOrden(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        CarritoDTO carritoDTO = carritoService.obtenerCarrito(usuarioId);

        if (carritoDTO.getItems().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear una orden con carrito vacío");
        }

        // Crear la orden
        Orden orden = new Orden();
        orden.setNumeroOrden(generarNumeroOrden());
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setTotal(carritoDTO.getTotal());
        orden.setEstado(Orden.EstadoOrden.PENDIENTE);
        orden.setUsuario(usuario);

        Orden ordenGuardada = ordenRepository.save(orden);

        // Crear los detalles de la orden
        List<DetalleOrden> detalles = new ArrayList<>();
        for (ItemCarritoDTO item : carritoDTO.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + item.getProductoId()));

            // Actualizar stock
            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetalleOrden detalle = new DetalleOrden();
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(item.getSubtotal());
            detalle.setOrden(ordenGuardada);
            detalle.setProducto(producto);

            detalles.add(detalle);
        }

        detalleOrdenRepository.saveAll(detalles);

        // Vaciar el carrito
        carritoService.vaciarCarrito(usuarioId);

        return convertirADTO(ordenGuardada, detalles);
    }

    @Override
    public OrdenDTO obtenerOrdenPorId(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        return convertirADTO(orden, new ArrayList<>(orden.getDetalles()));
    }

    @Override
    public List<OrdenDTO> obtenerOrdenesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        List<Orden> ordenes = ordenRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);

        return ordenes.stream()
                .map(orden -> convertirADTO(orden, new ArrayList<>(orden.getDetalles())))
                .collect(Collectors.toList());
    }

    @Override
    public OrdenDTO actualizarEstadoOrden(Long id, Orden.EstadoOrden estado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        orden.setEstado(estado);
        Orden ordenActualizada = ordenRepository.save(orden);

        return convertirADTO(ordenActualizada, new ArrayList<>(ordenActualizada.getDetalles()));
    }

    @Override
    @Transactional
    public void cancelarOrden(Long id) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        if (orden.getEstado() == Orden.EstadoOrden.ENTREGADO) {
            throw new IllegalArgumentException("No se puede cancelar una orden ya entregada");
        }

        // Restaurar stock
        for (DetalleOrden detalle : orden.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }

        orden.setEstado(Orden.EstadoOrden.CANCELADO);
        ordenRepository.save(orden);
    }

    private String generarNumeroOrden() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrdenDTO convertirADTO(Orden orden, List<DetalleOrden> detalles) {
        OrdenDTO ordenDTO = new OrdenDTO();
        ordenDTO.setId(orden.getId());
        ordenDTO.setNumeroOrden(orden.getNumeroOrden());
        ordenDTO.setFechaCreacion(orden.getFechaCreacion());
        ordenDTO.setTotal(orden.getTotal());
        ordenDTO.setEstado(orden.getEstado());
        ordenDTO.setUsuarioId(orden.getUsuario().getId());
        ordenDTO.setNombreCompleto(orden.getUsuario().getNombre() + " " + orden.getUsuario().getApellido());

        List<DetalleOrdenDTO> detallesDTO = detalles.stream()
                .map(detalle -> {
                    DetalleOrdenDTO detalleDTO = new DetalleOrdenDTO();
                    detalleDTO.setId(detalle.getId());
                    detalleDTO.setProductoId(detalle.getProducto().getId());
                    detalleDTO.setProductoNombre(detalle.getProducto().getNombre());
                    detalleDTO.setImagen(detalle.getProducto().getImagen());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                    detalleDTO.setSubtotal(detalle.getSubtotal());
                    return detalleDTO;
                })
                .collect(Collectors.toList());

        ordenDTO.setDetalles(detallesDTO);

        return ordenDTO;
    }
}