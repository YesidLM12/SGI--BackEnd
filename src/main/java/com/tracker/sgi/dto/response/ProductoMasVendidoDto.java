package com.tracker.sgi.dto.response;

import java.math.BigDecimal;

public record ProductoMasVendidoDto(
    String nombre_producto,
    Long cantidad_vendida,
    BigDecimal total_recaudado
) {

}
