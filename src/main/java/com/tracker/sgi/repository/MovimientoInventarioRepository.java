package com.tracker.sgi.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.dto.response.MovimientoResponseDto;
import com.tracker.sgi.entities.MovimientoInventario;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    Page<MovimientoInventario> findByProductoId(Long productoId, Pageable pageable);

    @Query("""
            SELECT new com.tracker.sgi.dto.response.MovimientoResponseDto(
                m.producto.nombre,
                m.tipo_movimiento,
                m.cantidad,
                m.orden.id,
                m.fecha_movimiento,
                m.stock_resultante
            )
            FROM MovimientoInventario m
            WHERE m.fecha_movimiento BETWEEN :fechaInicio AND :fechaFin
            ORDER BY m.fecha_movimiento DESC
            """)
    Page<MovimientoResponseDto> findMovimientosByDateRange(@Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin, Pageable pageable);
}
