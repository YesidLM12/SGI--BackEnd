package com.tracker.sgi.dto.response;

import com.tracker.sgi.util.enums.TipoMovimientoEnum;

import java.time.LocalDate;

public record MovimientoResponseDto(
				String producto,
				TipoMovimientoEnum tipo_movimiento,
				int cantidad,
				String motivo,
				Long orden_id,
				LocalDate fecha_movimiento,
				int stock_resultante
) {
}
