package com.tracker.sgi.dto.request;

import java.math.BigDecimal;

public record DetallesRequestDto(
    Long productoId,
    int cantidad,
    BigDecimal precioUnitario
) {

}
