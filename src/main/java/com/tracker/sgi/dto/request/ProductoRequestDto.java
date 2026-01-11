package com.tracker.sgi.dto.request;

import com.tracker.sgi.entities.Categorias;

import java.math.BigDecimal;

public record ProductoRequestDto(
				String nombre,
				BigDecimal precio,
				int stock_actual,
				int stock_minimo,
				boolean disponible,
				Categorias categoria
) {
}
