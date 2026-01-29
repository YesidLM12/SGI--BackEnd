package com.tracker.sgi.dto.request;

public record ProductoTopDto(
				Long productoId,
				String nombreProducto,
				Long cantidadVendida
) {
}
