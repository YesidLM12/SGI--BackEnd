package com.tracker.sgi.dto.request;

public record DashboardResumenDto(
				Long total_ventas,
				Long total_compras,
				Long ordenes_pendientes,
				Long productosStockBajo
) {
}
