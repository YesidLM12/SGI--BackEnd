package com.tracker.sgi.util.validations;

import java.math.BigDecimal;

import com.tracker.sgi.entities.Productos;
import com.tracker.sgi.exception.InvalidDataException;

public class ProductoValidate {

    public static void validate(Productos producto) {

        if (producto.getPrecio_compra() == null || producto.getPrecio_compra().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("El precio de compra debe ser mayor a cero");
        }

        if (producto.getPrecio_venta() == null || producto.getPrecio_venta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("El precio de venta debe ser mayor a cero");
        }

        if (producto.getStock_actual() < 0) {
            throw new InvalidDataException("El stock debe ser mayor o igual a cero");
        }

        if (producto.getStock_minimo() < 0) {
            throw new InvalidDataException("El stock mínimo debe ser mayor o igual a cero");
        }
    }

}
