package com.tracker.sgi.util.validations;

import java.math.BigDecimal;

import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.InvalidDataException;
import com.tracker.sgi.exception.InvalidStockMovementException;

public class ProductoValidate {

    public static void validate(Productos producto) {

        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("El precio debe ser mayor a cero");
        }

        if (producto.getStock_actual() < 0) {
            throw new InvalidDataException("El stock debe ser mayor o igual a cero");
        }

        if (producto.getStock_minimo() < 0) {
            throw new InvalidStockMovementException("El stock mínimo debe ser mayor o igual a cero");
        }
    }

}
