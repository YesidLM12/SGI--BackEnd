package com.tracker.sgi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDto(
    String nombre,
    BigDecimal precio,
    int stock_actual,
    int stock_minimo,
    String categoria,
    String proveedor
) {
}

