package com.tracker.sgi.dto.request;

public record ClienteRequestDto(
        String dni,
        String nombre,
        String apellido,
        String telefono,
        String email) {

}
