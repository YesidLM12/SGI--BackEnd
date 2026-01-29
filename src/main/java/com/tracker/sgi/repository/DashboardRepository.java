package com.tracker.sgi.repository;

import com.tracker.sgi.dto.request.*;
import com.tracker.sgi.entities.MovimientoInventario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardRepository  extends JpaRepository<MovimientoInventario, Long> {

	@Query("""
						SELECT
							SUM(CASE WHEN m.tipo_movimiento = 'VENTA' THEN m.cantidad ELSE 0 END) AS totalVentas,
							SUM(CASE WHEN m.tipo_movimiento = 'COMPRA' THEN m.cantidad ELSE 0 END) AS totalCompras,
							(SELECT COUNT(*) FROM Ordenes o WHERE o.estado = 'PENDIENTE') AS ordenesPendientes,
							(SELECT COUNT(*) FROM Productos p WHERE p.stock_actual <= p.stock_minimo) AS productosStockBajo
						FROM MovimientoInventario m
						WHERE m.fecha_movimiento BETWEEN :fechaInicio AND :fechaFin
					""")
	DashboardResumenProjection obtenerResumen(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

	@Query("""
				SELECT
					DATE_FORMAT(m.fecha_movimiento, '%Y-%m') AS periodo,
					SUM(CASE WHEN m.tipo_movimiento = 'COMPRA' THEN m.cantidad ELSE 0 END) AS totalEntradas,
					SUM(CASE WHEN m.tipo_movimiento = 'VENTA' THEN m.cantidad ELSE 0 END) AS totalSalidas
				FROM MovimientoInventario m
				WHERE m.fecha_movimiento BETWEEN :fechaInicio AND :fechaFin
				GROUP BY periodo
				ORDER BY periodo DESC
				""")
	List<MovimientoMensualProjection> obtenerMovimientoMensual(@Param("fechaInicio")LocalDateTime fechaInicio, @Param("fechaFin")LocalDateTime fechaFin);

	@Query("""
					SELECT
						p.id AS productoId,
						p.nombre AS nombreProducto,
						SUM(m.cantidad) AS cantidadVendida
					FROM MovimientoInventario m
					JOIN producto p ON p.id = m.producto.id
					WHERE m.tipo_movimiento = 'VENTA'
						AND m.fecha_movimiento BETWEEN :fechaInicio AND :fechaFin
					GROUP BY p.id, p.nombre
					ORDER BY cantidadVendida DESC
					LIMIT :limite
					""")
	List<ProductoTopProjection> obtenerProductosTop(
					@Param("fechaInicio")LocalDateTime fechaInicio,
					@Param("fechaFin")LocalDateTime fechaFin,
					@Param("limite")int limite
	);

	@Query("""
            SELECT new  com.tracker.sgi.dto.request.AlertaStockDto(
                p.nombre,
                p.stock_actual,
                p.stock_minimo,
                Case
                    When p.stock_actual <= p.stock_minimo
                        THEN com.tracker.sgi.util.enums.EstadoStockEnum.CRITICO
                    WHEN p.stock_actual <= p.stock_minimo * 2
                        THEN com.tracker.sgi.util.enums.EstadoStockEnum.BAJO
                ELSE com.tracker.sgi.util.enums.EstadoStockEnum.OK
                END
                )
            FROM Productos p
            WHERE p.stock_actual <= p.stock_minimo
            """)
	List<AlertasStockProjection> obtenerAlertasStock();

	@Query("""
				SELECT
					m.id AS movimientoId,
					m.tipo_movimiento As tipoMovimiento,
					m.cantidad AS cantidad,
					m.fecha_movimiento as fecha,
					u.nombre AS usuario
				FROM MovimientoInventario m
				JOIN Usuarios u ON u.id = m.usuario.id
				ORDER BY m.fecha_movimiento DESC
				LIMIT 10
				""")
	List<ActividadRecienteProjection> obtenerActividadReciente();
}
