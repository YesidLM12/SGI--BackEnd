package com.tracker.sgi.dto.request;

public record MovimientoMensualDto(
				String periodo,
				Long totalEntradas,
				Long totalSalidas
) {
}
