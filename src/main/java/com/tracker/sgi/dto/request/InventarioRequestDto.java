package com.tracker.sgi.dto.request;


public record InventarioRequestDto(
    Long productoId,
    int cantidad,
    String motivo,
    Long proveedorId,
    Long clienteId
){

}
