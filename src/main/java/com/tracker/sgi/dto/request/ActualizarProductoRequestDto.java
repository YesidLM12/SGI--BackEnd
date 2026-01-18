package com.tracker.sgi.dto.request;

import java.math.BigDecimal;

public record ActualizarProductoRequestDto(
    BigDecimal precio,
    int stock_minimo,
    boolean disponible
) {

}
