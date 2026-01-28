package com.tracker.sgi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tracker.sgi.dto.response.ProductoMasVendidoDto;
import com.tracker.sgi.entities.DetallesOrden;

@Repository
public interface DetallesOrdenRepository extends JpaRepository<DetallesOrden, Long> {

    @Query("""
            SELECT new com.tracker.sgi.dto.response.ProductoMasVendidoDto(
                p.nombre,
                SUM(d.cantidad),
                SUM(d.precio_unitario * d.cantidad)
            )
            FROM DetallesOrden d
            JOIN d.producto p
            JOIN d.orden o
            WHERE o.estado = 'COMPLETADA'
            GROUP BY p.id, p.nombre
            ORDER BY SUM(d.cantidad) DESC
            """)
    List<ProductoMasVendidoDto> findProductoMasVendido();
    

    @Query("""
            SELECT new com.tracker.sgi.dto.response.ProductoMasVendidoDto(
                p.nombre,
                SUM(d.cantidad),
                SUM(d.precio_unitario * d.cantidad)
            )
            FROM DetallesOrden d
            JOIN d.producto p
            JOIN d.orden o
            WHERE o.estado = 'COMPLETADA'
            AND o.fecha BETWEEN :fechaInicio AND :fechaFin
            GROUP BY p.id, p.nombre
            ORDER BY SUM(d.cantidad) DESC
            """)
    List<ProductoMasVendidoDto> findProductoMasVendidoPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("""
            SELECT new com.tracker.sgi.dto.response.ProductoMasVendidoDto(
                p.nombre,
                SUM(d.cantidad),
                SUM(d.precio_unitario * d.cantidad)
            )
            FROM DetallesOrden d
            JOIN d.producto p
            JOIN d.orden o
            JOIN p.categoria c
            WHERE o.estado = 'COMPLETADA'
            AND c.id = :categoriaId
            GROUP BY p.id, p.nombre
            ORDER BY SUM(d.cantidad) DESC
            """)
    List<ProductoMasVendidoDto> findProductoMasVendidoPorCategoria(Long categoriaId);
}
