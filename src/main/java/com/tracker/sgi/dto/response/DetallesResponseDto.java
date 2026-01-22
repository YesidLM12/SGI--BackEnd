package com.tracker.sgi.dto.response;

import java.math.BigDecimal;

public record DetallesResponseDto(
    String producto,
    int cantidad,
    BigDecimal precio_unitario
) {

}