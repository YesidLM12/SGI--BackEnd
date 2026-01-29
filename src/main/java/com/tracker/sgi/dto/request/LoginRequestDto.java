package com.tracker.sgi.dto.request;

import com.tracker.sgi.util.enums.EstadoStockEnum;

public record LoginRequestDto(
    String email,
    String password
) {
}
