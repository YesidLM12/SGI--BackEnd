package com.tracker.sgi.dto.request;

import com.tracker.sgi.util.enums.TipoMovimientoEnum;

import java.time.LocalDateTime;

public record ActividadRecienteDto(
				Long movimientoId,
				String tipoMovimiento,
				Integer cantidad,
				LocalDateTime fecha,
				String usuario
) {
}
