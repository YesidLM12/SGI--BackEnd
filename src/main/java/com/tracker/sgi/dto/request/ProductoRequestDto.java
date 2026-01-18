package com.tracker.sgi.dto.request;

import java.math.BigDecimal;

public record ProductoRequestDto(
				String nombre,
				BigDecimal precio,
				int stock_minimo,
				Long categoriaId
) {
}
