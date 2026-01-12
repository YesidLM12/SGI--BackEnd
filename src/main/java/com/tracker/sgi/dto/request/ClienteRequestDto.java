package com.tracker.sgi.dto.request;

public record ClienteRequestDto(
    String DNI,
    String nombre,
    String apellido,
    String telefono,
    String email
) {

}
