package com.tracker.sgi.dto.response;


import com.tracker.sgi.dto.request.*;

import java.util.List;

public record DashboardResponseDto(
				DashboardResumenDto resumen,
				List<MovimientoMensualDto> movimientosMensuales,
				List<ProductoTopDto> productoTop,
				List<AlertaStockDto> alertaStock,
				List<ActividadRecienteDto> actividadReciente
) {
}
