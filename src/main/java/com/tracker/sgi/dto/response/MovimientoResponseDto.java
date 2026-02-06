package com.tracker.sgi.dto.response;

import com.tracker.sgi.util.enums.TipoMovimientoEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovimientoResponseDto(
				String producto,
				TipoMovimientoEnum tipo_movimiento,
				int cantidad,
				Long orden_id,
				LocalDateTime fecha_movimiento,
				int stock_resultante
) {
}
