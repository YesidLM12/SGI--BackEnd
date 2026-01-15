package com.tracker.sgi.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Ordenes;
import com.tracker.sgi.util.enums.EstadoOrdenEnum;
import com.tracker.sgi.util.enums.TipoOrdenEnum;

@Repository
public interface OrdenRepository extends JpaRepository<Ordenes, Long> {

    Page<Ordenes> findByClienteId(Long clienteId, Pageable pageable);

    Page<Ordenes> findByProveedorId(Long proveedorId, Pageable pageable);

    Page<Ordenes> findByTipo(TipoOrdenEnum tipo, Pageable pageable);

    Page<Ordenes> findByEstado(EstadoOrdenEnum estado, Pageable pageable);

    Page<Ordenes> findByFecha(LocalDateTime fecha, Pageable pageable);

}
