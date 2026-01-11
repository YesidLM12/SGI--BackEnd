package com.tracker.sgi.dto.request;

public record InventarioRequestDto(
    String nombre,
    int cantidad,
    String motivo
) {

}
