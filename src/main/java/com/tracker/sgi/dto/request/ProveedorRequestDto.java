package com.tracker.sgi.dto.request;

public record ProveedorRequestDto(
    String nit_rut,
    String nombre,
    String telefono,
    String email,
    String direccion 
) {

}
