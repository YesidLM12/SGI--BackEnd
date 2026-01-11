package com.tracker.sgi.dto.request;

import com.tracker.sgi.entities.Productos;

public record InventarioRequestDto(
    Productos producto,
    int cantidad,
    String motivo
) {

}
