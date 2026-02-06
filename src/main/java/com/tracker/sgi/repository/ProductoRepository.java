package com.tracker.sgi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.entities.Productos;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Productos, Long> {
    @Query("""
                SELECT COALESCE(SUM(
                    CASE
                        WHEN m.tipo_movimiento = 'ENTRADA' THEN m.cantidad
                        WHEN m.tipo_movimiento = 'AJUSTE_POSITIVO' THEN m.cantidad
                        WHEN m.tipo_movimiento = 'SALIDA' THEN -m.cantidad
                        WHEN m.tipo_movimiento = 'AJUSTE_NEGATIVO' THEN -m.cantidad
                    END
                ), 0)
                FROM MovimientoInventario m
                WHERE m.producto.id = :productoId
            """)
    Integer calcularStockActual(@Param("productoId") Long productoId);

    Optional<Productos> findByNombre(String nombre);
}
