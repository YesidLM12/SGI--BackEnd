package com.tracker.sgi.util.validations;

import com.tracker.sgi.entities.Clientes;
import com.tracker.sgi.exception.InvalidDataException;

public class ClienteValidate {

    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("El email es obligatorio");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException("El email no es válido");
        }
        return false;
    }

    public static void validate(Clientes cliente) {
        validateEmail(cliente.getEmail());

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new InvalidDataException("El nombre es obligatorio");
        }

        if (cliente.getApellido() == null || cliente.getApellido().trim().isEmpty()) {
            throw new InvalidDataException("El apellido es obligatorio");
        }

        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new InvalidDataException("El telefono es obligatorio");
        }

        if (cliente.getDNI() == null || cliente.getDNI().trim().isEmpty()) {
            throw new InvalidDataException("El DNI es obligatorio");
        }

    }

}
