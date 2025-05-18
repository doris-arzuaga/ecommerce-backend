package com.arquitectura.ecommerce.repository;

import com.arquitectura.ecommerce.model.Orden;
import com.arquitectura.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);

    Optional<Orden> findByNumeroOrden(String numeroOrden);

    List<Orden> findByEstado(Orden.EstadoOrden estado);
}