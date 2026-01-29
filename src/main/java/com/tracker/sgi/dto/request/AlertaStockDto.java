package com.tracker.sgi.dto.request;

import com.tracker.sgi.util.enums.EstadoStockEnum;

public record AlertaStockDto(
				String nombreProducto,
				Integer stockActual,
				Integer stockMinimo,
				EstadoStockEnum estado
) {
}
