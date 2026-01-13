package com.tracker.sgi.util.validations;

import com.tracker.sgi.dto.request.ProveedorRequestDto;
import com.tracker.sgi.exception.InvalidDataException;

public class ProveedorValidate {
    
    public static void validate(ProveedorRequestDto dto) {
        
        if (dto.nit_rut() == null || dto.nit_rut().trim().isEmpty()) {
            throw new InvalidDataException("El nit_rut es obligatorio");
        }
        
        if (dto.nombre() == null || dto.nombre().trim().isEmpty()) {
            throw new InvalidDataException("El nombre es obligatorio");
        }

        if (dto.telefono() == null || dto.telefono().isEmpty()) {
            throw new InvalidDataException("El telefono es obligatorio");
        }

        if (dto.email() == null || dto.email().trim().isEmpty()) {
            throw new InvalidDataException("El email es obligatorio");
        }

        if (dto.direccion() == null || dto.direccion().trim().isEmpty()) {
            throw new InvalidDataException("La direccion es obligatoria");
        }
    }
}
