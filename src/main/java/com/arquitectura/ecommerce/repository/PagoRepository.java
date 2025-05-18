package com.arquitectura.ecommerce.repository;

import com.arquitectura.ecommerce.model.Pago;
import com.arquitectura.ecommerce.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByOrden(Orden orden);

    Optional<Pago> findByNumeroTransaccion(String numeroTransaccion);

    boolean existsByOrdenId(Long ordenId);
}