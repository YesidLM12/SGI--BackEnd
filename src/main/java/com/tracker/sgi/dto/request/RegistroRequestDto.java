package com.tracker.sgi.dto.request;

import com.tracker.sgi.util.enums.RolEnum;

public record RegistroRequestDto(
        String nombre,
        String email,
        String password,
        RolEnum rol,
        Boolean activo) {

}
