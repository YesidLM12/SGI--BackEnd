package com.tracker.sgi.dto.response;

import java.math.BigDecimal;

public record DetallesResponseDto(
    String productoId,
    int cantidad,
    BigDecimal precioUnitario
) {

}